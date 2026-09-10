package com.studyjava.lab.domain;

public record MemoryUsageInfo(long init, long used, long committed, long max, Double usageRatio) {}
