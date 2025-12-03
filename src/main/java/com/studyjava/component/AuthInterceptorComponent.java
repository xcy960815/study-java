package com.studyjava.component;

import com.studyjava.domain.enums.ResponseResultEnum;
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

    /**
     * 没有token的响应
     */
    private final String NOT_TOKEN_CONTENT = "{\"code\": " + ResponseResultEnum.NotToken.getCode() + ", \"message\": \"" + ResponseResultEnum.NotToken.getMessage() + "\"}";

    /**
     * token过期的响应
     */
    private final String INVALID_TOKEN_CONTENT = "{\"code\": " + ResponseResultEnum.InvalidToken.getCode() + ", \"message\": \"" + ResponseResultEnum.InvalidToken.getMessage() + "\"}";

    /**
     * contentType
     */
    private final String CONTENE_TYPE = "application/json;charset=UTF-8";

    @Resource
    private JwtTokenComponent jwtTokenComponent;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest httpServletRequest, @NonNull HttpServletResponse httpServletResponse, @NonNull Object handler) throws Exception {
        // 获取请求头上的token
        String authorization = getAuthorization(httpServletRequest);

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            // 如果用户没有携带token，或者不是以 Bearer 开头的token 给用户提示
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            httpServletResponse.setContentType(CONTENE_TYPE);
            httpServletResponse.getWriter().write(NOT_TOKEN_CONTENT);

            return false;
        }
        // 从用户的请求头上获取token
        String token = getToken(authorization);

        boolean isTokenExpired = jwtTokenComponent.isTokenExpired(token);

        if (isTokenExpired) {
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            httpServletResponse.setContentType(CONTENE_TYPE);
            httpServletResponse.getWriter().write(INVALID_TOKEN_CONTENT);
            return false;
        }
        return true;
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
     * 丛 authorization 中获取 token
     * @param authorization String
     * @return String
     */
    public String getToken(@NonNull String authorization){
        return authorization.substring(7);
    }
}

