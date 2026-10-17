package com.studyjava.domain.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 服务器监控历史采样记录
 *
 * <p>时序表高频写入、按时间清理，故不继承 BaseDao（避免逻辑删除与自动填充列拖慢保留策略）。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("study_java_server_metrics")
public class StudyJavaServerMetricDao implements Serializable {

  private static final long serialVersionUID = 1L;

  /** 主键ID */
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /** 指标键（cpu_usage/memory_usage/jvm_usage/disk_usage） */
  private String metricKey;

  /** 采样值（百分比） */
  private BigDecimal metricValue;

  /** 采样时间 */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private LocalDateTime sampledAt;
}
