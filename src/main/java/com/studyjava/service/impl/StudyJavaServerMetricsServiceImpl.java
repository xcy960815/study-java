package com.studyjava.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.studyjava.domain.dao.StudyJavaServerMetricDao;
import com.studyjava.domain.enums.StudyJavaServerMetricEnum;
import com.studyjava.domain.vo.StudyJavaServerInfoVo;
import com.studyjava.domain.vo.StudyJavaServerMetricsHistoryVo;
import com.studyjava.mapper.StudyJavaServerMetricMapper;
import com.studyjava.service.StudyJavaServerMetricsService;
import com.studyjava.service.StudyJavaServerMonitorService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 服务器监控历史采样服务实现
 *
 * <p>复用 {@link StudyJavaServerMonitorService#getServerInfo()} 的 oshi 快照， 每次采样把 CPU/内存/JVM/磁盘四个使用率写入
 * study_java_server_metrics。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StudyJavaServerMetricsServiceImpl implements StudyJavaServerMetricsService {

  /** 采样值统一保留两位小数，与 ServerInfoVo 展示口径一致 */
  private static final int VALUE_SCALE = 2;

  private final StudyJavaServerMonitorService serverMonitorService;

  private final StudyJavaServerMetricMapper serverMetricMapper;

  @Override
  public void sampleOnce() {
    StudyJavaServerInfoVo info = serverMonitorService.getServerInfo();
    if (info == null) {
      log.warn("服务器信息不可用，本轮采样跳过");
      return;
    }

    LocalDateTime sampledAt = LocalDateTime.now();
    List<StudyJavaServerMetricDao> records = new ArrayList<>();
    for (StudyJavaServerMetricEnum metric : StudyJavaServerMetricEnum.values()) {
      Double value;
      try {
        value = metric.getExtractor().apply(info);
      } catch (Exception e) {
        log.warn("指标 {} 取值失败，本轮跳过", metric.getKey(), e);
        continue;
      }
      if (value == null || !Double.isFinite(value)) {
        continue;
      }
      records.add(
          StudyJavaServerMetricDao.builder()
              .metricKey(metric.getKey())
              .metricValue(BigDecimal.valueOf(value).setScale(VALUE_SCALE, RoundingMode.HALF_UP))
              .sampledAt(sampledAt)
              .build());
    }

    if (records.isEmpty()) {
      log.warn("本轮采样没有可用指标");
      return;
    }
    serverMetricMapper.insertIgnoreBatch(records);
  }

  @Override
  public StudyJavaServerMetricsHistoryVo getHistory(String metricKey, long start, long end) {
    StudyJavaServerMetricEnum metric = StudyJavaServerMetricEnum.of(metricKey);
    if (metric == null) {
      throw new IllegalArgumentException("未知的监控指标：" + metricKey);
    }

    ZoneId zoneId = ZoneId.systemDefault();
    LocalDateTime startTime =
        LocalDateTime.ofInstant(java.time.Instant.ofEpochSecond(start), zoneId);
    LocalDateTime endTime = LocalDateTime.ofInstant(java.time.Instant.ofEpochSecond(end), zoneId);
    if (endTime.isBefore(startTime)) {
      LocalDateTime swap = startTime;
      startTime = endTime;
      endTime = swap;
    }

    List<StudyJavaServerMetricDao> records =
        serverMetricMapper.selectHistory(metric.getKey(), startTime, endTime);
    List<StudyJavaServerMetricsHistoryVo.Point> points =
        records.stream()
            .map(
                record ->
                    StudyJavaServerMetricsHistoryVo.Point.builder()
                        .timestamp(record.getSampledAt().atZone(zoneId).toEpochSecond())
                        .value(record.getMetricValue().doubleValue())
                        .build())
            .toList();

    return StudyJavaServerMetricsHistoryVo.builder()
        .metricKey(metric.getKey())
        .label(metric.getLabel())
        .unit(metric.getUnit())
        .points(points)
        .build();
  }

  @Override
  public int purgeBefore(LocalDateTime cutoff) {
    return serverMetricMapper.deleteSampledBefore(cutoff);
  }
}
