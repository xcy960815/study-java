package com.studyjava.service;

/** 订单号生成策略，抽象后可以替换为数据库号段或 Redis 实现。 */
public interface OrderNumberGenerator {

  String nextOrderNumber();
}
