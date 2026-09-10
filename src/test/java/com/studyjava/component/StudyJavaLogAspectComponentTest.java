package com.studyjava.component;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Proxy;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studyjava.annotation.Log;
import com.studyjava.domain.dao.StudyJavaSysOperLogDao;
import com.studyjava.domain.dto.StudyJavaSysUserDto;
import com.studyjava.service.StudyJavaSysOperLogService;

/** 操作日志切面的敏感字段脱敏回归测试 */
@ExtendWith(MockitoExtension.class)
class StudyJavaLogAspectComponentTest {

  @Mock private StudyJavaSysOperLogService operLogService;
  @Mock private JwtTokenComponent jwtTokenComponent;
  @Mock private JoinPoint joinPoint;
  @Mock private Signature signature;

  private StudyJavaLogAspectComponent aspect;

  @BeforeEach
  void setUp() {
    aspect = new StudyJavaLogAspectComponent();
    ReflectionTestUtils.setField(aspect, "studyJavaSysOperLogService", operLogService);
    ReflectionTestUtils.setField(aspect, "jwtTokenComponent", jwtTokenComponent);
    ReflectionTestUtils.setField(aspect, "objectMapper", new ObjectMapper());

    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader("Authorization", "Bearer test-token");
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

    when(jwtTokenComponent.getUserInfoFromAuthorization("Bearer test-token")).thenReturn("操作人");
    when(joinPoint.getTarget()).thenReturn(new Object());
    when(joinPoint.getSignature()).thenReturn(signature);
    when(signature.getName()).thenReturn("updateUser");
  }

  @AfterEach
  void clearRequestContext() {
    RequestContextHolder.resetRequestAttributes();
  }

  private Log logAnnotation() {
    return (Log)
        Proxy.newProxyInstance(
            Log.class.getClassLoader(),
            new Class<?>[] {Log.class},
            (proxy, method, args) ->
                switch (method.getName()) {
                  case "title" -> "用户管理";
                  case "businessType" ->
                      com.studyjava.domain.enums.BusinessType.UPDATE;
                  case "operatorType" ->
                      com.studyjava.domain.enums.OperatorType.MANAGE;
                  case "isSaveRequestData" -> true;
                  case "isSaveResponseData" -> false;
                  default -> method.getDefaultValue();
                });
  }

  @Test
  void masksPasswordFieldsInLoggedRequestParams() {
    StudyJavaSysUserDto payload = new StudyJavaSysUserDto();
    payload.setId(1001L);
    payload.setPasswordMd5("plain-old-password");
    payload.setNewPasswordMd5("plain-new-password");
    payload.setConfirmNewPasswordMd5("plain-new-password");
    when(joinPoint.getArgs()).thenReturn(new Object[] {payload});

    aspect.handleLog(joinPoint, logAnnotation(), null, null);

    ArgumentCaptor<StudyJavaSysOperLogDao> captor =
        ArgumentCaptor.forClass(StudyJavaSysOperLogDao.class);
    verify(operLogService).save(captor.capture());
    String operParam = captor.getValue().getOperParam();
    assertTrue(operParam.contains("\"passwordMd5\":\"***\""), operParam);
    assertTrue(operParam.contains("\"newPasswordMd5\":\"***\""), operParam);
    assertTrue(operParam.contains("\"confirmNewPasswordMd5\":\"***\""), operParam);
    assertEquals(false, operParam.contains("plain-old-password"));
    assertEquals(false, operParam.contains("plain-new-password"));
  }

  @Test
  void keepsNonSensitiveFieldsUntouched() {
    StudyJavaSysUserDto payload = new StudyJavaSysUserDto();
    payload.setId(1001L);
    payload.setLoginName("admin");
    payload.setPasswordMd5("secret");
    when(joinPoint.getArgs()).thenReturn(new Object[] {payload});

    aspect.handleLog(joinPoint, logAnnotation(), null, null);

    ArgumentCaptor<StudyJavaSysOperLogDao> captor =
        ArgumentCaptor.forClass(StudyJavaSysOperLogDao.class);
    verify(operLogService).save(captor.capture());
    String operParam = captor.getValue().getOperParam();
    assertTrue(operParam.contains("\"loginName\":\"admin\""), operParam);
    assertTrue(operParam.contains("\"id\":1001"), operParam);
    assertTrue(operParam.contains("\"passwordMd5\":\"***\""), operParam);
  }
}
