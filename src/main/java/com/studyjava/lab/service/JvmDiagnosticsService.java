package com.studyjava.lab.service;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.studyjava.exception.StudyJavaException;
import com.studyjava.lab.config.JavaLabManagementBeans;
import com.studyjava.lab.domain.GarbageCollectorInfo;
import com.studyjava.lab.domain.JvmSummary;
import com.studyjava.lab.domain.MemoryPoolInfo;
import com.studyjava.lab.domain.MemoryUsageInfo;
import com.studyjava.lab.domain.ThreadDumpEntry;
import com.studyjava.lab.domain.ThreadSummary;

@Service
@ConditionalOnProperty(prefix = "java-lab", name = "enabled", havingValue = "true")
public class JvmDiagnosticsService {

  private static final int MIN_STACK_DEPTH = 1;
  private static final int MAX_STACK_DEPTH = 200;

  private final MemoryMXBean memoryBean;
  private final List<MemoryPoolMXBean> memoryPoolBeans;
  private final List<GarbageCollectorMXBean> garbageCollectorBeans;
  private final ThreadMXBean threadBean;
  private final ClassLoadingMXBean classLoadingBean;
  private final RuntimeMXBean runtimeBean;

  public JvmDiagnosticsService(JavaLabManagementBeans beans) {
    this.memoryBean = beans.memory();
    this.memoryPoolBeans = beans.memoryPools();
    this.garbageCollectorBeans = beans.garbageCollectors();
    this.threadBean = beans.threads();
    this.classLoadingBean = beans.classLoading();
    this.runtimeBean = beans.runtime();
  }

  public JvmSummary getSummary() {
    long gcCount = garbageCollectorBeans.stream().mapToLong(this::nonNegativeCollectionCount).sum();
    long gcTime = garbageCollectorBeans.stream().mapToLong(this::nonNegativeCollectionTime).sum();
    return new JvmSummary(
        runtimeBean.getVmName(),
        runtimeBean.getVmVendor(),
        runtimeBean.getVmVersion(),
        Runtime.version().toString(),
        runtimeBean.getSpecVersion(),
        runtimeBean.getPid(),
        runtimeBean.getStartTime(),
        runtimeBean.getUptime(),
        toMemoryUsageInfo(memoryBean.getHeapMemoryUsage()),
        toMemoryUsageInfo(memoryBean.getNonHeapMemoryUsage()),
        threadBean.getThreadCount(),
        threadBean.getPeakThreadCount(),
        threadBean.getTotalStartedThreadCount(),
        classLoadingBean.getLoadedClassCount(),
        classLoadingBean.getTotalLoadedClassCount(),
        classLoadingBean.getUnloadedClassCount(),
        gcCount,
        gcTime);
  }

  public List<MemoryPoolInfo> getMemoryPools() {
    return memoryPoolBeans.stream()
        .map(
            bean ->
                new MemoryPoolInfo(
                    bean.getName(),
                    bean.getType(),
                    toMemoryUsageInfo(bean.getUsage()),
                    toMemoryUsageInfo(bean.getCollectionUsage())))
        .toList();
  }

  public List<GarbageCollectorInfo> getGarbageCollectors() {
    return garbageCollectorBeans.stream()
        .map(
            bean ->
                new GarbageCollectorInfo(
                    bean.getName(),
                    bean.getCollectionCount(),
                    bean.getCollectionTime(),
                    Arrays.asList(bean.getMemoryPoolNames())))
        .toList();
  }

  public ThreadSummary getThreadSummary() {
    ThreadInfo[] infos = threadBean.getThreadInfo(threadBean.getAllThreadIds(), 0);
    Map<Thread.State, Integer> states = countStates(infos);
    return new ThreadSummary(
        threadBean.getThreadCount(),
        threadBean.getPeakThreadCount(),
        threadBean.getDaemonThreadCount(),
        threadBean.getTotalStartedThreadCount(),
        states,
        threadBean.isThreadCpuTimeSupported(),
        threadBean.isCurrentThreadCpuTimeSupported(),
        threadBean.isThreadCpuTimeSupported() && threadBean.isThreadCpuTimeEnabled());
  }

  public List<ThreadDumpEntry> getThreadDump(int maxDepth) {
    validateMaxDepth(maxDepth);
    return toDumpEntries(threadBean.dumpAllThreads(true, true, maxDepth), maxDepth);
  }

  public List<ThreadDumpEntry> getDeadlocks(int maxDepth) {
    validateMaxDepth(maxDepth);
    long[] deadlockedIds;
    try {
      deadlockedIds = threadBean.findDeadlockedThreads();
    } catch (UnsupportedOperationException exception) {
      deadlockedIds = threadBean.findMonitorDeadlockedThreads();
    }
    if (deadlockedIds == null || deadlockedIds.length == 0) {
      return List.of();
    }
    ThreadInfo[] infos = threadBean.getThreadInfo(deadlockedIds, true, true, maxDepth);
    return toDumpEntries(infos, maxDepth);
  }

  static MemoryUsageInfo toMemoryUsageInfo(MemoryUsage usage) {
    if (usage == null) {
      return null;
    }
    Double ratio = usage.getMax() > 0 ? (double) usage.getUsed() / usage.getMax() : null;
    return new MemoryUsageInfo(
        usage.getInit(), usage.getUsed(), usage.getCommitted(), usage.getMax(), ratio);
  }

  static Map<Thread.State, Integer> countStates(ThreadInfo[] infos) {
    Map<Thread.State, Integer> states = emptyStateCounts();
    for (ThreadInfo info : infos) {
      if (info != null) {
        states.compute(info.getThreadState(), (state, count) -> count + 1);
      }
    }
    return Map.copyOf(states);
  }

  static void validateMaxDepth(int maxDepth) {
    if (maxDepth < MIN_STACK_DEPTH || maxDepth > MAX_STACK_DEPTH) {
      throw new StudyJavaException("maxDepth 必须在 1 到 200 之间");
    }
  }

  static ThreadDumpEntry toDumpEntry(ThreadInfo info, int maxDepth) {
    List<String> stack =
        Arrays.stream(info.getStackTrace())
            .limit(maxDepth)
            .map(StackTraceElement::toString)
            .toList();
    return new ThreadDumpEntry(
        info.getThreadId(),
        info.getThreadName(),
        info.getThreadState(),
        info.isDaemon(),
        info.getPriority(),
        info.getLockName(),
        info.getLockOwnerId() < 0 ? null : info.getLockOwnerId(),
        info.getLockOwnerName(),
        info.getBlockedCount(),
        info.getBlockedTime(),
        info.getWaitedCount(),
        info.getWaitedTime(),
        stack);
  }

  private List<ThreadDumpEntry> toDumpEntries(ThreadInfo[] infos, int maxDepth) {
    List<ThreadDumpEntry> result = new ArrayList<>();
    for (ThreadInfo info : infos) {
      if (info != null) {
        result.add(toDumpEntry(info, maxDepth));
      }
    }
    return List.copyOf(result);
  }

  private static Map<Thread.State, Integer> emptyStateCounts() {
    Map<Thread.State, Integer> states = new EnumMap<>(Thread.State.class);
    for (Thread.State state : Thread.State.values()) {
      states.put(state, 0);
    }
    return states;
  }

  private long nonNegativeCollectionCount(GarbageCollectorMXBean bean) {
    return Math.max(0, bean.getCollectionCount());
  }

  private long nonNegativeCollectionTime(GarbageCollectorMXBean bean) {
    return Math.max(0, bean.getCollectionTime());
  }
}
