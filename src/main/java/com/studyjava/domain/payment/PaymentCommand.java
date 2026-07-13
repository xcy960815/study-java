package com.studyjava.domain.payment;

/** 发送给支付策略的不可变命令。 */
public record PaymentCommand(String requestId, String orderNo, Integer amount) {}
