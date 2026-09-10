package com.studyjava.lab.domain;

import java.util.List;

public record ThreadDumpEntry(
    long threadId,
    String threadName,
    Thread.State state,
    boolean daemon,
    int priority,
    String lockName,
    Long lockOwnerId,
    String lockOwnerName,
    long blockedCount,
    long blockedTimeMillis,
    long waitedCount,
    long waitedTimeMillis,
    List<String> stackTrace) {
  public ThreadDumpEntry {
    stackTrace = List.copyOf(stackTrace);
  }
}
