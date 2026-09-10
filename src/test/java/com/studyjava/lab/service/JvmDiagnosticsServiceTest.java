package com.studyjava.lab.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.lang.management.ThreadInfo;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import com.studyjava.exception.StudyJavaException;
import com.studyjava.lab.config.JavaLabManagementBeans;
import com.studyjava.lab.domain.JvmSummary;
import com.studyjava.lab.domain.MemoryPoolInfo;
import com.studyjava.lab.domain.MemoryUsageInfo;
import com.studyjava.lab.domain.ThreadDumpEntry;

class JvmDiagnosticsServiceTest {

  private final JvmDiagnosticsService service = createService();

  @Test
  void collectsJvmDataAndAllMemoryPools() {
    JvmSummary summary = service.getSummary();
    List<MemoryPoolInfo> pools = service.getMemoryPools();

    assertNotNull(summary.vmName());
    assertTrue(summary.pid() > 0);
    assertTrue(summary.uptimeMillis() >= 0);
    assertNotNull(summary.heap());
    assertFalse(pools.isEmpty());
    assertEquals(ManagementFactory.getMemoryPoolMXBeans().size(), pools.size());
    assertEquals(
        ManagementFactory.getGarbageCollectorMXBeans().size(),
        service.getGarbageCollectors().size());
  }

  @Test
  void handlesUndefinedMaximumAndNullMemoryUsage() {
    MemoryUsageInfo info =
        JvmDiagnosticsService.toMemoryUsageInfo(new MemoryUsage(-1, 10, 20, -1));

    assertEquals(-1, info.max());
    assertNull(info.usageRatio());
    assertNull(JvmDiagnosticsService.toMemoryUsageInfo(null));
  }

  @Test
  void countsEveryThreadStateAndConvertsTrimmedDump() {
    ThreadInfo current =
        ManagementFactory.getThreadMXBean().getThreadInfo(Thread.currentThread().threadId(), 20);
    Map<Thread.State, Integer> states =
        JvmDiagnosticsService.countStates(new ThreadInfo[] {current});
    ThreadDumpEntry entry = JvmDiagnosticsService.toDumpEntry(current, 1);

    assertEquals(Thread.State.values().length, states.size());
    assertEquals(1, states.get(current.getThreadState()));
    assertTrue(entry.stackTrace().size() <= 1);
    assertEquals(current.getThreadName(), entry.threadName());
    assertNotNull(service.getThreadSummary().states());
  }

  @Test
  void validatesThreadDumpDepth() {
    assertThrows(StudyJavaException.class, () -> service.getThreadDump(0));
    assertThrows(StudyJavaException.class, () -> service.getThreadDump(201));
    assertFalse(service.getThreadDump(1).isEmpty());
  }

  @Test
  void returnsEmptyThenDetectsDaemonDeadlockWithoutBlockingTestExit() throws Exception {
    assertTrue(service.getDeadlocks(20).isEmpty());
    Object firstLock = new Object();
    Object secondLock = new Object();
    CountDownLatch firstAcquired = new CountDownLatch(1);
    CountDownLatch secondAcquired = new CountDownLatch(1);
    Thread first =
        daemonThread(
            "java-lab-deadlock-1",
            () -> lockInOppositeOrder(firstLock, secondLock, firstAcquired, secondAcquired));
    Thread second =
        daemonThread(
            "java-lab-deadlock-2",
            () -> lockInOppositeOrder(secondLock, firstLock, secondAcquired, firstAcquired));
    first.start();
    second.start();

    assertTrue(firstAcquired.await(1, TimeUnit.SECONDS));
    assertTrue(secondAcquired.await(1, TimeUnit.SECONDS));
    List<ThreadDumpEntry> deadlocks = waitForDeadlocks();

    assertTrue(
        deadlocks.stream().anyMatch(thread -> thread.threadName().equals("java-lab-deadlock-1")));
    assertTrue(
        deadlocks.stream().anyMatch(thread -> thread.threadName().equals("java-lab-deadlock-2")));
  }

  private static JvmDiagnosticsService createService() {
    return new JvmDiagnosticsService(
        new JavaLabManagementBeans(
            ManagementFactory.getMemoryMXBean(),
            ManagementFactory.getMemoryPoolMXBeans(),
            ManagementFactory.getGarbageCollectorMXBeans(),
            ManagementFactory.getThreadMXBean(),
            ManagementFactory.getClassLoadingMXBean(),
            ManagementFactory.getRuntimeMXBean()));
  }

  private static Thread daemonThread(String name, Runnable runnable) {
    Thread thread = new Thread(runnable, name);
    thread.setDaemon(true);
    return thread;
  }

  private List<ThreadDumpEntry> waitForDeadlocks() throws InterruptedException {
    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(3);
    List<ThreadDumpEntry> result = List.of();
    while (System.nanoTime() < deadline) {
      result = service.getDeadlocks(20);
      if (result.size() >= 2) {
        return result;
      }
      Thread.sleep(10);
    }
    return result;
  }

  private static void lockInOppositeOrder(
      Object ownLock, Object otherLock, CountDownLatch ownAcquired, CountDownLatch otherAcquired) {
    synchronized (ownLock) {
      ownAcquired.countDown();
      try {
        if (!otherAcquired.await(1, TimeUnit.SECONDS)) {
          return;
        }
      } catch (InterruptedException exception) {
        Thread.currentThread().interrupt();
        return;
      }
      synchronized (otherLock) {
        // Unreachable after both threads acquire their first monitor.
      }
    }
  }
}
