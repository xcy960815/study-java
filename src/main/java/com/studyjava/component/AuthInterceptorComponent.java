package com.studyjava.component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studyjava.annotation.PreAuthorize;
import com.studyjava.domain.vo.StudyJavaSysUserVo;
import com.studyjava.service.StudyJavaSysUserService;
import com.studyjava.utils.AuthRedisKeys;
import com.studyjava.utils.ErrorResponse;

import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthInterceptorComponent implements HandlerInterceptor {

  /** 权限缓存时长：角色/菜单变更最迟 5 分钟生效，登出时主动清除 */
  private static final long PERMISSIONS_CACHE_MINUTES = 5;

  private static final String CONTENT_TYPE = "application/json;charset=UTF-8";

  @Resource private JwtTokenComponent jwtTokenComponent;

  @Resource private RedisComponent redisComponent;

  @Resource private ObjectMapper objectMapper;

  @Resource private StudyJavaSysUserService studyJavaSysUserService;

  @Override
  public boolean preHandle(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull Object handler)
      throws Exception {
    // 获取请求头上的token
    String authorization = getAuthorization(request);

    if (authorization == null || !authorization.startsWith("Bearer ")) {
      sendErrorResponse(response, request.getRequestURI(), "未登录或Token缺失");
      return false;
    }

    // 从用户的请求头上获取token
    String token = getToken(authorization);

    boolean isTokenExpired = jwtTokenComponent.isTokenExpired(token);

    if (isTokenExpired) {
      sendErrorResponse(response, request.getRequestURI(), "Token已过期");
      return false;
    }

    String tokenUserInfo = jwtTokenComponent.getUserInfoFromToken(token);
    Map<String, Object> tokenUserInfoMap = JSONUtil.toBean(tokenUserInfo, Map.class);
    String userId = (String) tokenUserInfoMap.get("userId");
    String storedToken =
        redisComponent.get(AuthRedisKeys.accessTokenKey(userId), String.class);
    if (!token.equals(storedToken)) {
      sendErrorResponse(response, request.getRequestURI(), "Token无效或已退出登录");
      return false;
    }

    // 权限校验
    if (handler instanceof HandlerMethod handlerMethod) {
      PreAuthorize preAuthorize = handlerMethod.getMethodAnnotation(PreAuthorize.class);
      if (preAuthorize == null) {
        preAuthorize = handlerMethod.getBeanType().getAnnotation(PreAuthorize.class);
      }

      if (preAuthorize != null && !preAuthorize.value().isEmpty()) {
        if (!hasPermission(userId, preAuthorize.value())) {
          sendForbiddenResponse(response, request.getRequestURI(), "没有操作权限");
          return false;
        }
      }
    }

    return true;
  }

  /** 优先读 Redis 权限缓存，未命中时回源数据库并回填 */
  private boolean hasPermission(String userId, String permission) {
    String cacheKey = AuthRedisKeys.permissionsCacheKey(userId);
    List<String> permissions = readCachedPermissions(cacheKey);
    if (permissions == null) {
      StudyJavaSysUserVo userVo = studyJavaSysUserService.getUserInfo();
      if (userVo == null || userVo.getPermissions() == null) {
        return false;
      }
      permissions = userVo.getPermissions();
      cachePermissions(cacheKey, permissions);
    }
    return permissions.contains("*:*:*") || permissions.contains(permission);
  }

  @SuppressWarnings("unchecked")
  private List<String> readCachedPermissions(String cacheKey) {
    String cached = redisComponent.get(cacheKey, String.class);
    if (cached == null) {
      return null;
    }
    try {
      return objectMapper.readValue(cached, List.class);
    } catch (Exception exception) {
      log.warn("权限缓存解析失败，回源数据库: {}", exception.getMessage());
      return null;
    }
  }

  private void cachePermissions(String cacheKey, List<String> permissions) {
    try {
      redisComponent.setWithExpire(
          cacheKey,
          objectMapper.writeValueAsString(permissions),
          PERMISSIONS_CACHE_MINUTES,
          TimeUnit.MINUTES);
    } catch (Exception exception) {
      log.warn("权限缓存写入失败: {}", exception.getMessage());
    }
  }

  private void sendForbiddenResponse(HttpServletResponse response, String path, String message)
      throws Exception {
    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    response.setContentType(CONTENT_TYPE);
    ErrorResponse errorResponse =
        new ErrorResponse(HttpServletResponse.SC_FORBIDDEN, message, path);
    response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
  }

  private void sendErrorResponse(HttpServletResponse response, String path, String message)
      throws Exception {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType(CONTENT_TYPE);
    ErrorResponse errorResponse =
        new ErrorResponse(HttpServletResponse.SC_UNAUTHORIZED, message, path);
    response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
  }

  /**
   * 从请求头上获取 Authorization
   *
   * @param httpServletRequest HttpServletRequest
   * @return String
   */
  public String getAuthorization(@NonNull HttpServletRequest httpServletRequest) {
    // 获取请求头上的token
    return httpServletRequest.getHeader("Authorization");
  }

  /**
   * 从 authorization 中获取 token
   *
   * @param authorization String
   * @return String
   */
  public String getToken(@NonNull String authorization) {
    return authorization.substring(7);
  }
}
