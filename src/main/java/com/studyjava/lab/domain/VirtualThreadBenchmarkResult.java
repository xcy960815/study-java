package com.studyjava.lab.domain;

import java.util.List;

/** 虚拟线程基准测试聚合结果 */
public record VirtualThreadBenchmarkResult(
    String javaVersion,
    int taskCount,
    int sleepMillis,
    /** 全部引擎顺序执行的总耗时（毫秒） */
    long totalElapsedMillis,
    List<VirtualThreadEngineResult> engines) {

  public VirtualThreadBenchmarkResult {
    engines = List.copyOf(engines);
  }
}
