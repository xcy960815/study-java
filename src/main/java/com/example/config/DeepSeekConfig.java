package com.example.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

// 给yml文件添加deepseek的配置
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "deepseek")
public class DeepSeekConfig {

    private String apiKey;

}
