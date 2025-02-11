package com.example.component;

import com.example.domain.enums.ResponseResultEnum;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class AuthInterceptorComponent implements HandlerInterceptor {

    /**
     * 没有token的响应
     */
    private final String notTokenContent = "{\"code\": " + ResponseResultEnum.NotToken.getCode() + ", \"message\": \"" + ResponseResultEnum.NotToken.getMessage() + "\"}";

    /**
     * token过期的响应
     */
    private final String invalidTokenContent = "{\"code\": " + ResponseResultEnum.InvalidToken.getCode() + ", \"message\": \"" + ResponseResultEnum.InvalidToken.getMessage() + "\"}";


    private final String contentType = "application/json;charset=UTF-8";

    @Override
    public boolean preHandle(@NonNull HttpServletRequest httpServletRequest, @NonNull HttpServletResponse httpServletResponse, @NonNull Object handler) throws Exception {
        // 获取请求头上的token
        String authorization = httpServletRequest.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            // 如果用户没有携带token，或者不是以 Bearer 开头的token 给用户提示
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            httpServletResponse.setContentType(contentType);
            httpServletResponse.getWriter().write(notTokenContent);

            return false;
        }
        // 从用户的请求头上获取token
        String token = authorization.substring(7);

        boolean isTokenExpired = JwtTokenComponent.isTokenExpired(token);

        if (isTokenExpired) {
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            httpServletResponse.setContentType(contentType);
            httpServletResponse.getWriter().write(invalidTokenContent);
            return false;
        }
        return true;
    }
}
