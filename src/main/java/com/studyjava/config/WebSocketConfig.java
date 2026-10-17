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
    // 注册 WebSocket 端点（SockJS 传输，兼容旧版前端）
    registry
        .addEndpoint("/ws/server-monitor") // WebSocket 端点
        .setAllowedOriginPatterns("*") // 允许所有来源
        .withSockJS(); // 启用 SockJS 支持
    // 注册原生 WebSocket 端点：新版前端直连原生 STOMP，避免 sockjs-client 注册 unload
    // 监听器触发浏览器 Permissions policy violation（Chrome 115+ 默认禁用 unload）
    registry
        .addEndpoint("/ws/server-monitor-native") // 原生 WebSocket 端点
        .setAllowedOriginPatterns("*"); // 允许所有来源
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    // 配置消息代理
    registry.enableSimpleBroker("/topic"); // 使用简单的消息代理，前缀为 /topic
    registry.setApplicationDestinationPrefixes("/app"); // 应用程序目标前缀
  }
}
