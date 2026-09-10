package com.studyjava.lab.config;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.util.List;

public record JavaLabManagementBeans(
    MemoryMXBean memory,
    List<MemoryPoolMXBean> memoryPools,
    List<GarbageCollectorMXBean> garbageCollectors,
    ThreadMXBean threads,
    ClassLoadingMXBean classLoading,
    RuntimeMXBean runtime) {
  public JavaLabManagementBeans {
    memoryPools = List.copyOf(memoryPools);
    garbageCollectors = List.copyOf(garbageCollectors);
  }
}
