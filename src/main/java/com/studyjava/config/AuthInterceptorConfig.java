package com.studyjava.config;

import com.studyjava.component.AuthInterceptorComponent;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.util.Arrays;
import java.util.List;

/**
 * 验证身份拦截器
 */
@Configuration
public class AuthInterceptorConfig implements WebMvcConfigurer {

    @Resource
    private AuthInterceptorComponent authInterceptorComponent;

    // 不需要拦截的路径列表
    private static final List<String> EXCLUDE_PATHS = Arrays.asList(
            "/login",
            "/captcha",
            "/test/**",
            "/ollama/**",
            "/deepseek/**",
            "/v1/chat/completions"
    );

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptorComponent)
                .addPathPatterns("/**")
                .excludePathPatterns(EXCLUDE_PATHS); // 不需要拦截的路径;
    }
}

