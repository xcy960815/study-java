package com.studyjava.domain.vo;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 服务器监控指标历史趋势 VO */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudyJavaServerMetricsHistoryVo implements Serializable {

  private static final long serialVersionUID = 1L;

  /** 指标键 */
  @JsonProperty("metricKey")
  private String metricKey;

  /** 指标展示名 */
  @JsonProperty("label")
  private String label;

  /** 指标单位 */
  @JsonProperty("unit")
  private String unit;

  /** 趋势点列表（按时间升序） */
  @JsonProperty("points")
  private List<Point> points;

  /** 单个趋势点 */
  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Point implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 采样时间（Unix 秒级时间戳） */
    @JsonProperty("timestamp")
    private long timestamp;

    /** 采样值 */
    @JsonProperty("value")
    private double value;
  }
}
