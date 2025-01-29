package com.example.config;
import com.example.domain.enums.KaptchaConfigPropertyEnum;
import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KaptchaConfig {

    @Bean
    public Producer kaptchaProducer() {
        Properties properties = new Properties();
        // 使用枚举来设置属性
        for (KaptchaConfigPropertyEnum property : KaptchaConfigPropertyEnum.values()) {
            properties.setProperty(property.getKey(), property.getValue());
        }

        Config config = new Config(properties);
        DefaultKaptcha kaptcha = new DefaultKaptcha();
        kaptcha.setConfig(config);
        return kaptcha;
    }
}
