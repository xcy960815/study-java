package com.studyjava.lab.domain;

import java.util.List;

public record GarbageCollectorInfo(
    String name, long collectionCount, long collectionTimeMillis, List<String> memoryPoolNames) {
  public GarbageCollectorInfo {
    memoryPoolNames = List.copyOf(memoryPoolNames);
  }
}
