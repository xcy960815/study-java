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
            // 从用户的请求头上获取token
            String token = authHeader.substring(7);
            Boolean isTokenExpired = JwtTokenUtil.isTokenExpired(token);
            if (isTokenExpired) {
//              继续让接口请求的通 但是返回指定的code 让前端跳转到 登录页
                httpServletResponse.setStatus(HttpServletResponse.SC_OK);
//              保证汉字到了前端不乱码
                httpServletResponse.setContentType("application/json;charset=UTF-8");
                httpServletResponse.getWriter().write("{\"code\": 401, \"message\": \"Token已过期\"}");
                return false;
            }
            String name = JwtTokenUtil.getUsernameFromToken(token);
            System.out.println("当前登录用户是"+name);
            // 验证token
           Boolean valid =  JwtTokenUtil.validateToken(token, name);
           System.out.println(valid);
            return valid;
        } catch (Exception e) {
            e.printStackTrace(); // 打印错误信息
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }
}
