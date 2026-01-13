package com.studyjava.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class ThreadPoolConfig {

  @Bean("aiTaskExecutor")
  public Executor aiTaskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    // 核心线程数：根据您的机器配置和并发预期调整，IO密集型任务可以设大一点
    executor.setCorePoolSize(10);
    // 最大线程数
    executor.setMaxPoolSize(20);
    // 队列容量
    executor.setQueueCapacity(200);
    // 线程名前缀
    executor.setThreadNamePrefix("ai-executor-");
    // 拒绝策略：由调用者所在的线程执行
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    // 初始化
    executor.initialize();
    return executor;
  }
}
