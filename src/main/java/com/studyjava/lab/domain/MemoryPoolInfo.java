package com.studyjava.lab.domain;

import java.lang.management.MemoryType;

public record MemoryPoolInfo(
    String name, MemoryType type, MemoryUsageInfo usage, MemoryUsageInfo collectionUsage) {}
