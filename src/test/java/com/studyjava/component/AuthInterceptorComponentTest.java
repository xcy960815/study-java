package com.studyjava.component;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studyjava.annotation.PreAuthorize;
import com.studyjava.domain.vo.StudyJavaSysUserVo;
import com.studyjava.service.StudyJavaSysUserService;
import com.studyjava.utils.AuthRedisKeys;

@ExtendWith(MockitoExtension.class)
class AuthInterceptorComponentTest {

  private static final String CACHE_KEY = AuthRedisKeys.permissionsCacheKey("1001");

  @Mock private JwtTokenComponent jwtTokenComponent;
  @Mock private RedisComponent redisComponent;
  @Mock private StudyJavaSysUserService studyJavaSysUserService;

  private AuthInterceptorComponent interceptor;
  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    interceptor = new AuthInterceptorComponent();
    objectMapper = new ObjectMapper();
    ReflectionTestUtils.setField(interceptor, "jwtTokenComponent", jwtTokenComponent);
    ReflectionTestUtils.setField(interceptor, "redisComponent", redisComponent);
    ReflectionTestUtils.setField(interceptor, "objectMapper", objectMapper);
    ReflectionTestUtils.setField(interceptor, "studyJavaSysUserService", studyJavaSysUserService);

    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setRequestURI("/user/getUserList");
    request.addHeader("Authorization", "Bearer test-token");
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

    lenient().when(jwtTokenComponent.isTokenExpired("test-token")).thenReturn(false);
    lenient()
        .when(jwtTokenComponent.getUserInfoFromToken("test-token"))
        .thenReturn("{\"userId\":\"1001\",\"loginName\":\"admin\"}");
    lenient()
        .when(redisComponent.get(AuthRedisKeys.accessTokenKey("1001"), String.class))
        .thenReturn("test-token");
  }

  @AfterEach
  void clearRequestContext() {
    RequestContextHolder.resetRequestAttributes();
  }

  private HandlerMethod annotatedHandler() throws NoSuchMethodException {
    return new HandlerMethod(new SampleController(), SampleController.class.getMethod("listUsers"));
  }

  private StudyJavaSysUserVo userVoWithPermissions(List<String> permissions) {
    StudyJavaSysUserVo userVo = new StudyJavaSysUserVo();
    userVo.setId(1001L);
    userVo.setLoginName("admin");
    userVo.setPermissions(permissions);
    return userVo;
  }

  @Test
  void loadsPermissionsOnCacheMissAndWritesBack() throws Exception {
    when(redisComponent.get(CACHE_KEY, String.class)).thenReturn(null);
    when(studyJavaSysUserService.getUserInfo())
        .thenReturn(userVoWithPermissions(List.of("system:user:list")));

    MockHttpServletResponse response = new MockHttpServletResponse();
    boolean allowed = interceptor.preHandle(request(), response, annotatedHandler());

    assertTrue(allowed);
    verify(redisComponent)
        .setWithExpire(
            CACHE_KEY,
            objectMapper.writeValueAsString(List.of("system:user:list")),
            5L,
            TimeUnit.MINUTES);
  }

  @Test
  void cacheHitSkipsDatabaseLookup() throws Exception {
    when(redisComponent.get(CACHE_KEY, String.class))
        .thenReturn(objectMapper.writeValueAsString(List.of("system:user:list")));

    MockHttpServletResponse response = new MockHttpServletResponse();
    boolean allowed = interceptor.preHandle(request(), response, annotatedHandler());

    assertTrue(allowed);
    verify(studyJavaSysUserService, never()).getUserInfo();
    verify(redisComponent, never()).setWithExpire(anyString(), anyString(), anyLong(), any());
  }

  @Test
  void deniesRequestWhenPermissionMissing() throws Exception {
    when(redisComponent.get(CACHE_KEY, String.class))
        .thenReturn(objectMapper.writeValueAsString(List.of("system:order:list")));

    MockHttpServletResponse response = new MockHttpServletResponse();
    boolean allowed = interceptor.preHandle(request(), response, annotatedHandler());

    assertFalse(allowed);
    assertEquals(403, response.getStatus());
    verify(studyJavaSysUserService, never()).getUserInfo();
  }

  private MockHttpServletRequest request() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setRequestURI("/user/getUserList");
    request.addHeader("Authorization", "Bearer test-token");
    return request;
  }

  static class SampleController {
    @PreAuthorize("system:user:list")
    public void listUsers() {}
  }
}
