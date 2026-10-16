package com.studyjava.lab.domain;

/** 单个执行引擎的基准测试结果 */
public record VirtualThreadEngineResult(
    /** 引擎标识，如 platform-8 / virtual */
    String engineKey,
    /** 引擎名称（中文展示用） */
    String label,
    /** 平台线程池：PLATFORM；虚拟线程：VIRTUAL */
    String threadType,
    /** 并发力度：平台线程池为池大小，虚拟线程引擎≈任务数（每任务一线程） */
    int workerCount,
    int taskCount,
    /** 该引擎整轮完成耗时（毫秒） */
    long wallMillis,
    /** 吞吐量 = taskCount * 1000 / wallMillis */
    long throughputPerSecond,
    /** 该引擎运行期间 JVM 平台线程峰值（thread dump 语义，不含虚拟线程） */
    int peakPlatformThreads) {}
