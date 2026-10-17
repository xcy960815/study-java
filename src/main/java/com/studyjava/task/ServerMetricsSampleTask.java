package com.studyjava.task;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.studyjava.service.StudyJavaServerMetricsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 服务器监控历史采样任务
 *
 * <p>每分钟采集一次 CPU/内存/JVM/磁盘使用率写入历史表（独立于 WebSocket 的 3 秒实时推送， 无论页面是否打开都持续积累趋势数据）；每天 3:10 清理 35
 * 天前的过期采样。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ServerMetricsSampleTask {

  /** 采样数据保留天数 */
  private static final int RETENTION_DAYS = 35;

  private final StudyJavaServerMetricsService serverMetricsService;

  /** 每分钟采样一次，首轮延迟 20 秒等待应用完成初始化 */
  @Scheduled(fixedDelay = 60_000, initialDelay = 20_000)
  public void sampleMetrics() {
    try {
      serverMetricsService.sampleOnce();
    } catch (Exception e) {
      log.error("服务器监控指标采样失败", e);
    }
  }

  /** 每天 3:10 清理过期采样数据（保留 35 天） */
  @Scheduled(cron = "0 10 3 * * ?")
  public void purgeHistory() {
    try {
      int deleted = serverMetricsService.purgeBefore(LocalDateTime.now().minusDays(RETENTION_DAYS));
      if (deleted > 0) {
        log.info("服务器监控历史清理完成，删除 {} 条过期采样", deleted);
      }
    } catch (Exception e) {
      log.error("服务器监控历史清理失败", e);
    }
  }
}
