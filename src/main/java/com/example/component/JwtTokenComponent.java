package com.example.component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenComponent {

    //    从yml文件中获取配置 demo
    //    @Value("${jwt.secret}")
    //    private String secretKey; // 注入的 secretKey

    @Resource
    private RedisComponent redisComponent;

    /**
     * 秘钥在redis中的名称
     */
    private static final String SECRET_KEY_NAME = "study-java-secret-key";

    /**
     * 设置过期时间（1天）
     */
    private final long EXPIRATION_TIME = 86400000L;

    /**
     * 秘钥
     */
    private Key SECRET_KEY;

    /**
     * 初始化 SECRET_KEY
     */
    @PostConstruct
    public void initSecretKey() {
        /* 先从redis里面取 */
        if (redisComponent.hasKey(SECRET_KEY_NAME)) {
            // 从 Redis 获取字节数组
             String secretKeyName = redisComponent.get(SECRET_KEY_NAME,String.class);
            // 通过字节数组构造 Key
            this.SECRET_KEY = new SecretKeySpec(Base64.getDecoder().decode(secretKeyName), SignatureAlgorithm.HS512.getJcaName());
        }else {
            this.SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
            redisComponent.setWithExpire(SECRET_KEY_NAME, Base64.getEncoder().encodeToString(SECRET_KEY.getEncoded()),EXPIRATION_TIME, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 生成 Token
     * @param tokenContent String
     * @return String
     */
    public String generateToken(String tokenContent) {
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
    public String getUserInfoFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }

    /**
     * 校验token是否过期
     * @param token string
     * @return boolean
     */
    public boolean isTokenExpired(String token) {
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
    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}
