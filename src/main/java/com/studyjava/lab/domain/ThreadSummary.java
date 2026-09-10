package com.studyjava.lab.domain;

import java.util.Map;

public record ThreadSummary(
    int threadCount,
    int peakThreadCount,
    int daemonThreadCount,
    long totalStartedThreadCount,
    Map<Thread.State, Integer> states,
    boolean threadCpuTimeSupported,
    boolean currentThreadCpuTimeSupported,
    boolean threadCpuTimeEnabled) {
  public ThreadSummary {
    states = Map.copyOf(states);
  }
}
