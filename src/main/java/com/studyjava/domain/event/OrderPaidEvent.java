package com.studyjava.domain.event;

/** 订单支付事务成功后发送给其他模块的不可变事件。 */
public record OrderPaidEvent(
    Long orderId,
    String orderNo,
    Long userId,
    Integer amount,
    Integer paymentType,
    String transactionNo) {}
