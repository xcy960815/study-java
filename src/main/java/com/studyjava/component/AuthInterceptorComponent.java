package com.studyjava.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studyjava.utils.ErrorResponse;
import jakarta.annotation.Resource;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class AuthInterceptorComponent implements HandlerInterceptor {

    private static final String CONTENT_TYPE = "application/json;charset=UTF-8";

    @Resource
    private JwtTokenComponent jwtTokenComponent;

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
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
        return true;
    }

    private void sendErrorResponse(HttpServletResponse response, String path, String message) throws Exception {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(CONTENT_TYPE);
        ErrorResponse errorResponse = new ErrorResponse(HttpServletResponse.SC_UNAUTHORIZED, message, path);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }

    /**
     * 从请求头上获取 Authorization
     * @param httpServletRequest HttpServletRequest
     * @return String
     */
    public String getAuthorization(@NonNull HttpServletRequest httpServletRequest){
        // 获取请求头上的token
        return httpServletRequest.getHeader("Authorization");
    }

    /**
     * 从 authorization 中获取 token
     * @param authorization String
     * @return String
     */
    public String getToken(@NonNull String authorization){
        return authorization.substring(7);
    }
}

