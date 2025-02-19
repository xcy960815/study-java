package com.example.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 跨域配置
 */
@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 默认超时时间
     */
    private static final long DEFAULT_TIMEOUT = 60000L;  // 1分钟的超时时间 (单位毫秒)

    /**
     * 永不超时
     */
    private static final long NEVER_TIMEOUT = -1L;

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        // 默认设置超时时间为1分钟
        configurer.setDefaultTimeout(NEVER_TIMEOUT);
//        TODO

        // 自定义的拦截器
//        configurer.registerCallableInterceptors((request, response, handler) -> {
//            // 判断请求路径，如果是以 "ollama" 开头的接口，设置为永不超时
//            if (request.getRequestURI().startsWith("/ollama")) {
//                configurer.setDefaultTimeout(-1L);  // 永不超时
//            } else {
//                configurer.setDefaultTimeout(60000L);  // 其他接口超时为1分钟
//            }
//        });
    }

    /**
     * 跨域配置
     * @param registry CorsRegistry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*") // 允许的前端地址
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(false); // 允许携带cookie
    }
}
