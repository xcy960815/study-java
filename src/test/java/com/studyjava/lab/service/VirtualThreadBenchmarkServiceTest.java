package com.studyjava.lab.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import com.studyjava.exception.StudyJavaException;
import com.studyjava.lab.domain.VirtualThreadBenchmarkRequest;
import com.studyjava.lab.domain.VirtualThreadBenchmarkResult;
import com.studyjava.lab.domain.VirtualThreadEngineResult;

class VirtualThreadBenchmarkServiceTest {

  private final VirtualThreadBenchmarkService service = new VirtualThreadBenchmarkService();

  @Test
  void runsAllEnginesWithConsistentStats() {
    VirtualThreadBenchmarkResult result = service.run(new VirtualThreadBenchmarkRequest(80, 5));

    assertEquals(3, result.engines().size());
    assertEquals(80, result.taskCount());
    assertEquals(5, result.sleepMillis());
    assertTrue(result.totalElapsedMillis() >= 0);
    assertEquals("platform-8", result.engines().get(0).engineKey());
    assertEquals("virtual", result.engines().get(2).engineKey());

    for (VirtualThreadEngineResult engine : result.engines()) {
      assertEquals(80, engine.taskCount());
      assertTrue(engine.wallMillis() >= 0);
      assertTrue(engine.throughputPerSecond() >= 0);
      assertTrue(engine.peakPlatformThreads() > 0);
    }

    // 虚拟线程引擎每任务一线程，并发力度等于任务数
    VirtualThreadEngineResult virtualEngine = result.engines().get(2);
    assertEquals("VIRTUAL", virtualEngine.threadType());
    assertEquals(80, virtualEngine.workerCount());
  }

  @Test
  void clampsBelowLowerBoundary() {
    VirtualThreadBenchmarkResult result = service.run(new VirtualThreadBenchmarkRequest(10, 1));

    assertEquals(50, result.taskCount());
    assertEquals(5, result.sleepMillis());
  }

  @Test
  void clampsSleepAboveUpperBoundary() {
    VirtualThreadBenchmarkResult result = service.run(new VirtualThreadBenchmarkRequest(80, 9_999));

    assertEquals(80, result.taskCount());
    assertEquals(50, result.sleepMillis());
  }

  @Test
  void rejectsConcurrentRuns() throws Exception {
    VirtualThreadBenchmarkRequest request = new VirtualThreadBenchmarkRequest(200, 50);
    CompletableFuture<VirtualThreadBenchmarkResult> first =
        CompletableFuture.supplyAsync(() -> service.run(request));

    // 等待首个基准真正开始，再触发第二个请求
    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(10);
    while (!service.isRunning() && !first.isDone()) {
      assertTrue(System.nanoTime() < deadline, "首个基准测试迟迟未开始");
      TimeUnit.MILLISECONDS.sleep(10);
    }

    try {
      if (!first.isDone()) {
        StudyJavaException exception =
            assertThrows(StudyJavaException.class, () -> service.run(request));
        assertEquals("基准测试正在运行，请稍候再试", exception.getMessage());
      }
      VirtualThreadBenchmarkResult firstResult = first.get(30, TimeUnit.SECONDS);
      assertEquals(200, firstResult.taskCount());
    } finally {
      assertFalse(service.isRunning());
    }
  }
}
