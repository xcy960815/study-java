package com.studyjava.component;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.regex.Pattern;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studyjava.annotation.Log;
import com.studyjava.domain.dao.StudyJavaSysOperLogDao;
import com.studyjava.service.StudyJavaSysOperLogService;
import com.studyjava.utils.IpUtils;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/** 操作日志记录处理 */
@Aspect
@Component
@Slf4j
public class StudyJavaLogAspectComponent {
  @Resource private StudyJavaSysOperLogService studyJavaSysOperLogService;

  @Resource private JwtTokenComponent jwtTokenComponent;

  @Resource private ObjectMapper objectMapper;

  private static final ThreadLocal<Long> TIME_THREADLOCAL = new NamedThreadLocal<>("Cost Time");

  /** 敏感字段名（不区分大小写），序列化参数前替换其值为 *** */
  private static final Pattern SENSITIVE_FIELD_PATTERN =
      Pattern.compile(
          "(\"(?:passwordMd5|newPasswordMd5|confirmNewPasswordMd5|password|oldPassword)\""
              + "\\s*:\\s*)\"[^\"]*\"",
          Pattern.CASE_INSENSITIVE);

  /** 处理请求前执行 */
  @Before(value = "@annotation(controllerLog)")
  public void doBefore(JoinPoint joinPoint, Log controllerLog) {
    TIME_THREADLOCAL.set(System.currentTimeMillis());
  }

  /**
   * 处理完请求后执行
   *
   * @param joinPoint 切点
   */
  @AfterReturning(pointcut = "@annotation(controllerLog)", returning = "jsonResult")
  public void doAfterReturning(JoinPoint joinPoint, Log controllerLog, Object jsonResult) {
    handleLog(joinPoint, controllerLog, null, jsonResult);
  }

  /**
   * 拦截异常操作
   *
   * @param joinPoint 切点
   * @param e 异常
   */
  @AfterThrowing(value = "@annotation(controllerLog)", throwing = "e")
  public void doAfterThrowing(JoinPoint joinPoint, Log controllerLog, Exception e) {
    handleLog(joinPoint, controllerLog, e, null);
  }

  protected void handleLog(
      final JoinPoint joinPoint, Log controllerLog, final Exception e, Object jsonResult) {
    try {
      // 获取当前的用户
      HttpServletRequest request =
          ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

      // *========数据库日志=========*//
      StudyJavaSysOperLogDao operLog = new StudyJavaSysOperLogDao();
      operLog.setStatus(0);
      // 请求的地址
      String ip = IpUtils.getIpAddr(request);
      operLog.setOperIp(ip);
      operLog.setOperUrl(request.getRequestURI());

      String token = request.getHeader("Authorization");
      if (token != null && token.startsWith("Bearer ")) {
        String username = jwtTokenComponent.getUserInfoFromAuthorization(token);
        operLog.setOperName(username);
      }

      if (e != null) {
        operLog.setStatus(1);
        operLog.setErrorMsg(
            e.getMessage() != null && e.getMessage().length() > 2000
                ? e.getMessage().substring(0, 2000)
                : e.getMessage());
      }
      // 设置方法名称
      String className = joinPoint.getTarget().getClass().getName();
      String methodName = joinPoint.getSignature().getName();
      operLog.setMethod(className + "." + methodName + "()");
      // 设置请求方式
      operLog.setRequestMethod(request.getMethod());
      // 处理设置注解上的参数
      getControllerMethodDescription(joinPoint, controllerLog, operLog, jsonResult);
      // 设置消耗时间
      operLog.setOperTime(new Date());
      Long startTime = TIME_THREADLOCAL.get();
      if (startTime != null) {
        operLog.setCostTime(System.currentTimeMillis() - startTime);
      } else {
        operLog.setCostTime(0L);
      }

      // 保存数据库
      studyJavaSysOperLogService.save(operLog);
    } catch (Exception exp) {
      // 记录本地异常日志
      log.error("操作日志记录失败: {}", exp.getMessage(), exp);
    } finally {
      TIME_THREADLOCAL.remove();
    }
  }

  /**
   * 获取注解中对方法的描述信息 用于Controller层注解
   *
   * @param log 日志
   * @param operLog 操作日志
   * @throws Exception
   */
  public void getControllerMethodDescription(
      JoinPoint joinPoint, Log log, StudyJavaSysOperLogDao operLog, Object jsonResult)
      throws Exception {
    // 设置action动作
    operLog.setBusinessType(log.businessType().ordinal());
    // 设置标题
    operLog.setTitle(log.title());
    // 设置操作人类别
    operLog.setOperatorType(log.operatorType().ordinal());
    // 是否需要保存request，参数和值
    if (log.isSaveRequestData()) {
      // 获取参数的信息，传入到数据库中。
      setRequestValue(joinPoint, operLog);
    }
    // 是否需要保存response，参数和值
    if (log.isSaveResponseData() && jsonResult != null) {
      String json = maskSensitiveFields(objectMapper.writeValueAsString(jsonResult));
      operLog.setJsonResult(json != null && json.length() > 2000 ? json.substring(0, 2000) : json);
    }
  }

  /**
   * 获取请求的参数，放到log中
   *
   * @param operLog 操作日志
   * @throws Exception 异常
   */
  private void setRequestValue(JoinPoint joinPoint, StudyJavaSysOperLogDao operLog)
      throws Exception {
    String params = argsArrayToString(joinPoint.getArgs());
    operLog.setOperParam(
        params != null && params.length() > 2000 ? params.substring(0, 2000) : params);
  }

  /** 参数拼装 */
  private String argsArrayToString(Object[] paramsArray) {
    StringBuilder params = new StringBuilder();
    if (paramsArray != null && paramsArray.length > 0) {
      for (Object o : paramsArray) {
        if (o != null && !isFilterObject(o)) {
          try {
            String jsonObj = objectMapper.writeValueAsString(o);
            params.append(maskSensitiveFields(jsonObj)).append(' ');
          } catch (Exception e) {
            log.warn("操作日志参数序列化失败: {}", e.getMessage());
          }
        }
      }
    }
    return params.toString().trim();
  }

  /** 将 JSON 文本中敏感字段的值替换为 ***，避免密码等落库 */
  private String maskSensitiveFields(String json) {
    return SENSITIVE_FIELD_PATTERN.matcher(json).replaceAll("$1\"***\"");
  }

  /**
   * 判断是否需要过滤的对象。
   *
   * @param o 对象信息。
   * @return 如果是需要过滤的对象，则返回true；否则返回false。
   */
  @SuppressWarnings("rawtypes")
  public boolean isFilterObject(final Object o) {
    Class<?> clazz = o.getClass();
    if (clazz.isArray()) {
      return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
    } else if (Collection.class.isAssignableFrom(clazz)) {
      Collection collection = (Collection) o;
      for (Object value : collection) {
        if (value instanceof MultipartFile) {
          return true;
        }
      }
    } else if (Map.class.isAssignableFrom(clazz)) {
      Map map = (Map) o;
      for (Object value : map.entrySet()) {
        Map.Entry entry = (Map.Entry) value;
        if (entry.getValue() instanceof MultipartFile) {
          return true;
        }
      }
    }
    return o instanceof MultipartFile
        || o instanceof HttpServletRequest
        || o instanceof HttpServletResponse
        || o instanceof BindingResult;
  }
}
