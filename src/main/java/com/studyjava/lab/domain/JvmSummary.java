package com.studyjava.lab.domain;

public record JvmSummary(
    String vmName,
    String vmVendor,
    String vmVersion,
    String javaRuntimeVersion,
    String javaSpecificationVersion,
    long pid,
    long startTimeMillis,
    long uptimeMillis,
    MemoryUsageInfo heap,
    MemoryUsageInfo nonHeap,
    int threadCount,
    int peakThreadCount,
    long totalStartedThreadCount,
    int loadedClassCount,
    long totalLoadedClassCount,
    long unloadedClassCount,
    long gcCollectionCount,
    long gcCollectionTimeMillis) {}
