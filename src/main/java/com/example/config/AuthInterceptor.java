package com.example.config;



import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import com.example.utils.JwtTokenUtil;


@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        String authHeader = httpServletRequest.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        try {
            String token = authHeader.substring(7);
            System.out.println("Token received: " + token); // 调试输出
//            Claims claims = Jwts.parser()
//                    .setSigningKey(JwtTokenUtil.getSecretKey())
//                    .parseClaimsJws(token)
//                    .getBody();
//            System.out.println("Claims: " + claims); // 调试输出
            return true;
        } catch (Exception e) {
            e.printStackTrace(); // 打印错误信息
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }
}
