package com.studyjava.lab.controller;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.studyjava.annotation.PreAuthorize;
import com.studyjava.lab.domain.GarbageCollectorInfo;
import com.studyjava.lab.domain.JvmSummary;
import com.studyjava.lab.domain.MemoryPoolInfo;
import com.studyjava.lab.domain.ThreadDumpEntry;
import com.studyjava.lab.domain.ThreadSummary;
import com.studyjava.lab.service.JvmDiagnosticsService;

@RestController
@RequestMapping("/lab/jvm")
@ConditionalOnProperty(prefix = "java-lab", name = "enabled", havingValue = "true")
public class JvmLabController {

  private final JvmDiagnosticsService diagnosticsService;

  public JvmLabController(JvmDiagnosticsService diagnosticsService) {
    this.diagnosticsService = diagnosticsService;
  }

  @GetMapping("/summary")
  @PreAuthorize("monitor:jvm:query")
  public JvmSummary summary() {
    return diagnosticsService.getSummary();
  }

  @GetMapping("/memory-pools")
  @PreAuthorize("monitor:jvm:query")
  public List<MemoryPoolInfo> memoryPools() {
    return diagnosticsService.getMemoryPools();
  }

  @GetMapping("/gc")
  @PreAuthorize("monitor:jvm:query")
  public List<GarbageCollectorInfo> garbageCollectors() {
    return diagnosticsService.getGarbageCollectors();
  }

  @GetMapping("/threads/summary")
  @PreAuthorize("monitor:jvm:query")
  public ThreadSummary threadSummary() {
    return diagnosticsService.getThreadSummary();
  }

  @GetMapping("/threads/dump")
  @PreAuthorize("monitor:jvm:thread")
  public List<ThreadDumpEntry> threadDump(
      @RequestParam(defaultValue = "100") int maxDepth) {
    return diagnosticsService.getThreadDump(maxDepth);
  }

  @GetMapping("/threads/deadlocks")
  @PreAuthorize("monitor:jvm:thread")
  public List<ThreadDumpEntry> deadlocks(
      @RequestParam(defaultValue = "100") int maxDepth) {
    return diagnosticsService.getDeadlocks(maxDepth);
  }
}
