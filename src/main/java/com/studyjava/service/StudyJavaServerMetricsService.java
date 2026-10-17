package com.studyjava.service;

import java.time.LocalDateTime;

import com.studyjava.domain.vo.StudyJavaServerMetricsHistoryVo;

/** 服务器监控历史采样服务 */
public interface StudyJavaServerMetricsService {

  /**
   * 采样一次全部指标并写入历史表。
   *
   * <p>单个指标取值失败会跳过该指标，不影响其余指标落库。
   */
  void sampleOnce();

  /**
   * 查询指标在指定区间内的历史趋势。
   *
   * @param metricKey 指标键（见 {@link com.studyjava.domain.enums.StudyJavaServerMetricEnum}）
   * @param start Unix 秒级起始时间（含）
   * @param end Unix 秒级结束时间（含）
   * @return 趋势数据（label/unit/points）
   * @throws IllegalArgumentException 指标键不存在时抛出
   */
  StudyJavaServerMetricsHistoryVo getHistory(String metricKey, long start, long end);

  /**
   * 物理删除 cutoff 之前的采样记录。
   *
   * @param cutoff 截止时间（不含）
   * @return 删除条数
   */
  int purgeBefore(LocalDateTime cutoff);
}
