package com.studyjava.lab.service;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.stereotype.Service;

import com.studyjava.exception.StudyJavaException;
import com.studyjava.lab.domain.VirtualThreadBenchmarkRequest;
import com.studyjava.lab.domain.VirtualThreadBenchmarkResult;
import com.studyjava.lab.domain.VirtualThreadEngineResult;

import lombok.extern.slf4j.Slf4j;

/**
 * 虚拟线程基准测试服务：同一阻塞型负载下对比平台线程池与虚拟线程的吞吐表现。
 *
 * <p>负载为 Thread.sleep 模拟的纯阻塞 IO 任务（park 语义，无 CPU/内存放大）， 演示安全；参数通过夹取限制在安全范围内，且全局同时只允许一个基准在跑。
 */
@Slf4j
@Service
public class VirtualThreadBenchmarkService {

  /** 上界按“最长引擎 8 线程池 + 50ms 阻塞”可在 HTTP 超时内完成推算 */
  private static final int MIN_TASK_COUNT = 50;

  private static final int MAX_TASK_COUNT = 3_000;
  private static final int MIN_SLEEP_MILLIS = 5;
  private static final int MAX_SLEEP_MILLIS = 50;

  private static final int DEFAULT_TASK_COUNT = 2_000;
  private static final int DEFAULT_SLEEP_MILLIS = 20;

  private static final String ENGINE_PLATFORM_8 = "platform-8";
  private static final String ENGINE_PLATFORM_64 = "platform-64";
  private static final String ENGINE_VIRTUAL = "virtual";

  private final AtomicBoolean running = new AtomicBoolean(false);

  /** 运行基准测试；同一时刻仅允许一个任务，重复触发直接拒绝 */
  public VirtualThreadBenchmarkResult run(VirtualThreadBenchmarkRequest request) {
    if (!running.compareAndSet(false, true)) {
      throw new StudyJavaException("基准测试正在运行，请稍候再试");
    }
    try {
      return doRun(request);
    } finally {
      running.set(false);
    }
  }

  /** 透出运行状态供并发触发测试/监控使用 */
  public boolean isRunning() {
    return running.get();
  }

  private VirtualThreadBenchmarkResult doRun(VirtualThreadBenchmarkRequest request) {
    int taskCount =
        clamp(
            intValue(request, VirtualThreadBenchmarkRequest::taskCount),
            MIN_TASK_COUNT,
            MAX_TASK_COUNT,
            DEFAULT_TASK_COUNT);
    int sleepMillis =
        clamp(
            intValue(request, VirtualThreadBenchmarkRequest::sleepMillis),
            MIN_SLEEP_MILLIS,
            MAX_SLEEP_MILLIS,
            DEFAULT_SLEEP_MILLIS);

    long totalStart = System.nanoTime();
    List<VirtualThreadEngineResult> engines = new ArrayList<>(3);
    engines.add(
        runEngine(
            ENGINE_PLATFORM_8,
            "平台线程池 × 8",
            "PLATFORM",
            8,
            () -> Executors.newFixedThreadPool(8),
            taskCount,
            sleepMillis));
    engines.add(
        runEngine(
            ENGINE_PLATFORM_64,
            "平台线程池 × 64",
            "PLATFORM",
            64,
            () -> Executors.newFixedThreadPool(64),
            taskCount,
            sleepMillis));
    engines.add(
        runEngine(
            ENGINE_VIRTUAL,
            "虚拟线程（每任务一线程）",
            "VIRTUAL",
            taskCount,
            Executors::newVirtualThreadPerTaskExecutor,
            taskCount,
            sleepMillis));

    return new VirtualThreadBenchmarkResult(
        System.getProperty("java.version", "unknown"),
        taskCount,
        sleepMillis,
        elapsedMillis(totalStart),
        engines);
  }

  private VirtualThreadEngineResult runEngine(
      String engineKey,
      String label,
      String threadType,
      int workerCount,
      ExecutorFactory executorFactory,
      int taskCount,
      int sleepMillis) {
    ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
    threadBean.resetPeakThreadCount();
    long engineStart = System.nanoTime();
    try (ExecutorService executor = executorFactory.create()) {
      List<Callable<Object>> tasks = new ArrayList<>(taskCount);
      for (int i = 0; i < taskCount; i++) {
        tasks.add(
            () -> {
              // sleep 走 park 语义：虚拟线程在阻塞时释放载体线程
              Thread.sleep(sleepMillis);
              return null;
            });
      }
      List<Future<Object>> futures = new ArrayList<>(taskCount);
      for (Callable<Object> task : tasks) {
        futures.add(executor.submit(task));
      }
      // 单任务超时兜底，防止任何异常挂死 HTTP 请求
      for (Future<Object> future : futures) {
        future.get(90, TimeUnit.SECONDS);
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new StudyJavaException("基准测试被中断");
    } catch (Exception e) {
      log.error("虚拟线程基准引擎 {} 执行失败", engineKey, e);
      throw new StudyJavaException("基准测试执行失败：" + e.getMessage());
    }
    int peakPlatformThreads = threadBean.getPeakThreadCount();
    long wallMillis = elapsedMillis(engineStart);
    long throughputPerSecond = wallMillis <= 0 ? 0 : taskCount * 1000L / wallMillis;
    return new VirtualThreadEngineResult(
        engineKey,
        label,
        threadType,
        workerCount,
        taskCount,
        wallMillis,
        throughputPerSecond,
        peakPlatformThreads);
  }

  private interface ExecutorFactory {
    ExecutorService create();
  }

  private static int intValue(
      VirtualThreadBenchmarkRequest request,
      java.util.function.Function<VirtualThreadBenchmarkRequest, Integer> extractor) {
    Integer value = request == null ? null : extractor.apply(request);
    // null 与负值统一走 clamp 的默认值分支
    return value == null ? -1 : value;
  }

  private static int clamp(int value, int min, int max, int fallback) {
    if (value < min) {
      return value < 0 ? fallback : min;
    }
    if (value > max) {
      return max;
    }
    return value;
  }

  private static long elapsedMillis(long nanoStart) {
    return TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - nanoStart);
  }
}
