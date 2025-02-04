package com.example.config;

import com.example.component.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.util.Arrays;
import java.util.List;

// 验证身份拦截器
@Configuration
public class AuthInterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor authInterceptor;


    // 不需要拦截的路径列表
    private static final List<String> EXCLUDE_PATHS = Arrays.asList(
            "/login",
            "/captcha",
            "/vform/test",
            "/vform/test1",
            "/vform/test2",
            "/sse/stream"
    );

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(EXCLUDE_PATHS); // 不需要拦截的路径;
    }
}

