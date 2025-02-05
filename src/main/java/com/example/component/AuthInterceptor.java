package com.example.component;

import com.example.domain.enums.ResponseResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        log.info("AuthInterceptor triggered for: {}",httpServletRequest.getRequestURI());
        String requestURI = httpServletRequest.getRequestURI();
        log.info("当前请求路径是 {}", requestURI);
        String authorization = httpServletRequest.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            return false;
        }
        try {
            // 从用户的请求头上获取token
            String token = authorization.substring(7);
            boolean isTokenExpired = JwtTokenUtil.isTokenExpired(token);
            if (isTokenExpired) {
                // 继续让接口请求的通 但是返回指定的code
                httpServletResponse.setStatus(HttpServletResponse.SC_OK);
                // 保证汉字到了前端不乱码
                httpServletResponse.setContentType("application/json;charset=UTF-8");
                httpServletResponse.getWriter().write("{\"code\": " + ResponseResultEnum.InvalidToken.getCode() + ", \"message\": \"" + ResponseResultEnum.InvalidToken.getMessage() + "\"}");

                return false;
            }
            // String userInfoStr = JwtTokenUtil.getUserInfoFromToken(token);
            return true;

        } catch (Exception e) {
//            e.printStackTrace(); // 打印错误信息
            log.error("AuthInterceptor error: " + e);
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }
}
