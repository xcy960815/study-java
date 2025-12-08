package com.studyjava.controller.monitor;

import com.studyjava.domain.vo.StudyJavaServerInfoVo;
import com.studyjava.service.StudyJavaServerMonitorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 服务器监控控制器
 *
 * <p>优化说明：不再在 Controller 中管理定时任务，改为调用 Service 层的监控启动/停止方法
 */
@Slf4j
@RestController
@RequestMapping("/monitor/server")
@RequiredArgsConstructor
public class StudyJavaServerMonitorController {

  private final StudyJavaServerMonitorService serverMonitorService;
  private final SimpMessagingTemplate messagingTemplate;
  private ScheduledExecutorService webSocketPushScheduler;

  /**
   * 获取服务器信息（单次请求）
   *
   * @return 服务器信息
   */
  @GetMapping("/info")
  public ResponseEntity<StudyJavaServerInfoVo> getServerInfo() {
    StudyJavaServerInfoVo serverInfo = serverMonitorService.getServerInfo();
    return ResponseEntity.ok(serverInfo);
  }

  /**
   * 开始推送服务器监控数据
   *
   * @return 操作结果
   */
  @PostMapping("/start")
  public ResponseEntity<String> startPush() {
    // 启动 Service 层的监控
    boolean started = serverMonitorService.startMonitoring();
    if (!started) {
      return ResponseEntity.ok("监控已在运行中");
    }

    // 启动 WebSocket 推送
    if (webSocketPushScheduler != null && !webSocketPushScheduler.isShutdown()) {
      return ResponseEntity.ok("WebSocket 推送已在运行中");
    }

    webSocketPushScheduler =
        Executors.newSingleThreadScheduledExecutor(
            r -> {
              Thread thread = new Thread(r, "websocket-push-scheduler");
              thread.setDaemon(true);
              return thread;
            });

    webSocketPushScheduler.scheduleAtFixedRate(
        () -> {
          try {
            // 直接获取缓存数据（无阻塞）
            StudyJavaServerInfoVo serverInfo = serverMonitorService.getServerInfo();
            if (serverInfo != null) {
              // 通过 WebSocket 推送数据到 /topic/server-monitor
              messagingTemplate.convertAndSend("/topic/server-monitor", serverInfo);
              log.debug("推送服务器监控数据: {}", serverInfo);
            }
          } catch (Exception e) {
            log.error("推送服务器监控数据失败", e);
          }
        },
        0,
        3,
        TimeUnit.SECONDS);

    log.info("开始推送服务器监控数据，间隔：3秒");
    return ResponseEntity.ok("开始推送监控数据");
  }

  /**
   * 停止推送服务器监控数据
   *
   * @return 操作结果
   */
  @PostMapping("/stop")
  public ResponseEntity<String> stopPush() {
    // 停止 Service 层的监控
    serverMonitorService.stopMonitoring();

    // 停止 WebSocket 推送
    if (webSocketPushScheduler != null && !webSocketPushScheduler.isShutdown()) {
      webSocketPushScheduler.shutdown();
      try {
        if (!webSocketPushScheduler.awaitTermination(3, TimeUnit.SECONDS)) {
          webSocketPushScheduler.shutdownNow();
        }
      } catch (InterruptedException e) {
        webSocketPushScheduler.shutdownNow();
        Thread.currentThread().interrupt();
      }
      log.info("停止推送服务器监控数据");
      return ResponseEntity.ok("停止推送监控数据");
    }
    return ResponseEntity.ok("监控未在运行");
  }
}
