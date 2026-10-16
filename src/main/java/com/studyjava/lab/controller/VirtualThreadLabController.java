package com.studyjava.lab.controller;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.studyjava.annotation.PreAuthorize;
import com.studyjava.lab.domain.VirtualThreadBenchmarkRequest;
import com.studyjava.lab.domain.VirtualThreadBenchmarkResult;
import com.studyjava.lab.service.VirtualThreadBenchmarkService;

/** 虚拟线程实验室：同一阻塞型负载下对比平台线程池与虚拟线程吞吐 */
@RestController
@RequestMapping("/lab/virtual-thread")
@ConditionalOnProperty(prefix = "java-lab", name = "enabled", havingValue = "true")
public class VirtualThreadLabController {

  private final VirtualThreadBenchmarkService benchmarkService;

  public VirtualThreadLabController(VirtualThreadBenchmarkService benchmarkService) {
    this.benchmarkService = benchmarkService;
  }

  /**
   * 运行基准测试（最长约 20 秒，前端调用需放宽 axios 超时）
   *
   * @param request 可空，缺省走服务端默认参数（2000 任务 × 20ms 阻塞）
   */
  @PostMapping("/benchmark")
  @PreAuthorize("monitor:vthread:query")
  public VirtualThreadBenchmarkResult benchmark(
      @RequestBody(required = false) VirtualThreadBenchmarkRequest request) {
    return benchmarkService.run(request);
  }
}
