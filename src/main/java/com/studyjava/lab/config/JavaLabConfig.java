package com.studyjava.lab.config;

import java.lang.management.ManagementFactory;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "java-lab", name = "enabled", havingValue = "true")
public class JavaLabConfig {

  @Bean
  JavaLabManagementBeans javaLabManagementBeans() {
    return new JavaLabManagementBeans(
        ManagementFactory.getMemoryMXBean(),
        ManagementFactory.getMemoryPoolMXBeans(),
        ManagementFactory.getGarbageCollectorMXBeans(),
        ManagementFactory.getThreadMXBean(),
        ManagementFactory.getClassLoadingMXBean(),
        ManagementFactory.getRuntimeMXBean());
  }
}
