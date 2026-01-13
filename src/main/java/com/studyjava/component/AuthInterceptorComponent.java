package com.studyjava.component;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studyjava.utils.ErrorResponse;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthInterceptorComponent implements HandlerInterceptor {

  private static final String CONTENT_TYPE = "application/json;charset=UTF-8";

  @Resource private JwtTokenComponent jwtTokenComponent;

  @Resource private ObjectMapper objectMapper;

  @Resource private com.studyjava.service.StudyJavaSysUserService studyJavaSysUserService;

  @Resource private com.studyjava.mapper.StudyJavaSysMenuMapper studyJavaSysMenuMapper;

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

    // 权限校验
    if (handler instanceof org.springframework.web.method.HandlerMethod handlerMethod) {
      com.studyjava.annotation.PreAuthorize preAuthorize =
          handlerMethod.getMethodAnnotation(com.studyjava.annotation.PreAuthorize.class);
      if (preAuthorize == null) {
        preAuthorize =
            handlerMethod.getBeanType().getAnnotation(com.studyjava.annotation.PreAuthorize.class);
      }

      if (preAuthorize != null && !preAuthorize.value().isEmpty()) {
        String permission = preAuthorize.value();
        // 获取当前用户信息（包含权限）
        com.studyjava.domain.vo.StudyJavaSysUserVo userVo = studyJavaSysUserService.getUserInfo();
        if (userVo == null
            || userVo.getPermissions() == null
            || (!userVo.getPermissions().contains("*:*:*")
                && !userVo.getPermissions().contains(permission))) {
          sendForbiddenResponse(response, request.getRequestURI(), "没有操作权限");
          return false;
        }
      }
    }

    return true;
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
