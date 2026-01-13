package com.studyjava.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

// 给yml文件添加deepseek的配置
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "deepseek.api")
public class DeepSeekConfig {

  private String key;

  private String baseUrl = "https://api.deepseek.com"; // 默认值
}
