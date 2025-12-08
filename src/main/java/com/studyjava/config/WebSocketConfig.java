package com.studyjava.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/** WebSocket 配置 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    // 注册 WebSocket 端点
    registry
        .addEndpoint("/ws/server-monitor") // WebSocket 端点
        .setAllowedOriginPatterns("*") // 允许所有来源
        .withSockJS(); // 启用 SockJS 支持
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    // 配置消息代理
    registry.enableSimpleBroker("/topic"); // 使用简单的消息代理，前缀为 /topic
    registry.setApplicationDestinationPrefixes("/app"); // 应用程序目标前缀
  }
}
