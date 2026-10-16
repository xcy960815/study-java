package com.studyjava.lab.domain;

/**
 * 虚拟线程基准测试请求参数
 *
 * <p>字段为可空包装类型，后端负责校验与夹取： taskCount ∈ [50, 3000]，sleepMillis ∈ [5, 50]，超出范围按边界值处理并保留默认值兜底。
 */
public record VirtualThreadBenchmarkRequest(Integer taskCount, Integer sleepMillis) {}
