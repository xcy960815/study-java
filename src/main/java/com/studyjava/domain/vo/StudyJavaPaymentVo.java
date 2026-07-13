package com.studyjava.domain.vo;

/** 支付结果。idempotent=true 表示本次请求复用了此前已经完成的支付结果。 */
public record StudyJavaPaymentVo(
    Long orderId,
    String orderNo,
    Integer totalPrice,
    Integer orderStatus,
    Integer payStatus,
    Integer payType,
    String transactionNo,
    boolean idempotent) {}
