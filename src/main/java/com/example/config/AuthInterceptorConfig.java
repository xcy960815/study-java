package com.example.config;

import com.example.component.AuthInterceptorComponent;
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
    private AuthInterceptorComponent authInterceptorComponent;


    // 不需要拦截的路径列表
    private static final List<String> EXCLUDE_PATHS = Arrays.asList(
            "/login",
            "/captcha",
            "/vform/test",
            "/vform/test1",
            "/vform/test2"
    );

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptorComponent)
                .addPathPatterns("/**")
                .excludePathPatterns(EXCLUDE_PATHS); // 不需要拦截的路径;
    }
}

