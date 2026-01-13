package com.studyjava.domain.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 服务器监控信息 VO */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudyJavaServerInfoVo implements Serializable {
  private static final long serialVersionUID = 1L;

  /** CPU 信息 */
  @JsonProperty("cpu")
  private CpuInfo cpu;

  /** 内存信息 */
  @JsonProperty("memory")
  private MemoryInfo memory;

  /** JVM 信息 */
  @JsonProperty("jvm")
  private JvmInfo jvm;

  /** 磁盘信息 */
  @JsonProperty("disk")
  private DiskInfo disk;

  /** 系统信息 */
  @JsonProperty("sys")
  private SysInfo sys;

  /** CPU 信息 */
  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class CpuInfo implements Serializable {
    /** CPU 核心数 */
    @JsonProperty("cpuNum")
    private int cpuNum;

    /** CPU 使用率 (%) */
    @JsonProperty("used")
    private double used;

    /** CPU 空闲率 (%) */
    @JsonProperty("free")
    private double free;

    /** CPU 型号 */
    @JsonProperty("cpuModel")
    private String cpuModel;
  }

  /** 内存信息 */
  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class MemoryInfo implements Serializable {
    /** 总内存 (GB) */
    @JsonProperty("total")
    private double total;

    /** 已使用内存 (GB) */
    @JsonProperty("used")
    private double used;

    /** 空闲内存 (GB) */
    @JsonProperty("free")
    private double free;

    /** 使用率 (%) */
    @JsonProperty("usage")
    private double usage;
  }

  /** JVM 信息 */
  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class JvmInfo implements Serializable {
    /** JVM 总内存 (MB) */
    @JsonProperty("total")
    private double total;

    /** JVM 最大可使用内存 (MB) */
    @JsonProperty("max")
    private double max;

    /** JVM 已使用内存 (MB) */
    @JsonProperty("used")
    private double used;

    /** JVM 空闲内存 (MB) */
    @JsonProperty("free")
    private double free;

    /** JVM 使用率 (%) */
    @JsonProperty("usage")
    private double usage;

    /** JVM 版本 */
    @JsonProperty("version")
    private String version;

    /** JVM 启动时间 */
    @JsonProperty("startTime")
    private String startTime;

    /** JVM 运行时长 */
    @JsonProperty("runTime")
    private String runTime;
  }

  /** 磁盘信息 */
  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class DiskInfo implements Serializable {
    /** 总空间 (GB) */
    @JsonProperty("total")
    private double total;

    /** 已使用空间 (GB) */
    @JsonProperty("used")
    private double used;

    /** 可用空间 (GB) */
    @JsonProperty("free")
    private double free;

    /** 使用率 (%) */
    @JsonProperty("usage")
    private double usage;
  }

  /** 系统信息 */
  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class SysInfo implements Serializable {
    /** 操作系统 */
    @JsonProperty("os")
    private String os;

    /** 系统架构 */
    @JsonProperty("arch")
    private String arch;

    /** 服务器名称 */
    @JsonProperty("hostname")
    private String hostname;

    /** 服务器 IP */
    @JsonProperty("ip")
    private String ip;
  }
}
