package com.studyjava.domain.enums;

import java.util.function.Function;

import com.studyjava.domain.vo.StudyJavaServerInfoVo;

import lombok.Getter;

/** 服务器监控历史采样指标 */
@Getter
public enum StudyJavaServerMetricEnum {

  /** CPU 使用率（%） */
  CPU_USAGE("cpu_usage", "CPU 使用率", "%", info -> safeValue(info.getCpu().getUsed())),

  /** 内存使用率（%） */
  MEMORY_USAGE("memory_usage", "内存使用率", "%", info -> safeValue(info.getMemory().getUsage())),

  /** JVM 内存使用率（%） */
  JVM_USAGE("jvm_usage", "JVM 内存使用率", "%", info -> safeValue(info.getJvm().getUsage())),

  /** 磁盘使用率（%） */
  DISK_USAGE("disk_usage", "磁盘使用率", "%", info -> safeValue(info.getDisk().getUsage()));

  /** 指标键（对外接口与存储使用） */
  private final String key;

  /** 指标展示名 */
  private final String label;

  /** 指标单位 */
  private final String unit;

  /** 从服务器信息 VO 中提取该指标的取值函数 */
  private final Function<StudyJavaServerInfoVo, Double> extractor;

  StudyJavaServerMetricEnum(
      String key, String label, String unit, Function<StudyJavaServerInfoVo, Double> extractor) {
    this.key = key;
    this.label = label;
    this.unit = unit;
    this.extractor = extractor;
  }

  /**
   * 按指标键查找枚举。
   *
   * @param key 指标键
   * @return 对应枚举；未知键返回 null
   */
  public static StudyJavaServerMetricEnum of(String key) {
    for (StudyJavaServerMetricEnum metric : values()) {
      if (metric.key.equals(key)) {
        return metric;
      }
    }
    return null;
  }

  /**
   * 空安全取值：源数据缺失或非有限数时返回 null，由采样流程跳过该指标。
   *
   * @param value 原始取值
   * @return 可存储的有限数值，否则 null
   */
  private static Double safeValue(Double value) {
    if (value == null || !Double.isFinite(value)) {
      return null;
    }
    return value;
  }
}
