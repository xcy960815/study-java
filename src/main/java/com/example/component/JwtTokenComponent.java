package com.example.component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenComponent {

    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    // private static final long EXPIRATION_TIME = 60000L; // 设置过期时间（1分钟）

    private static final long EXPIRATION_TIME = 86400000L; // 设置过期时间（1天）


    // 生成 Token
    public static String generateToken(String tokenContent) {
        return Jwts.builder()
                .setSubject(tokenContent)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    /**
     * 从 Token 中获取用户信息
     * @param token String
     * @return String
     */
    public static String getUserInfoFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }

    /**
     * 校验token是否过期
     * @param token string
     * @return boolean
     */
    public static boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true; // 解析出错时默认视为过期
        }
    }

    /**
     * 从 Token 中获取 Claims
     * @param token String
     * @return Claims
     */
    private static Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}
