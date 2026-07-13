package com.studyjava.domain.order;

import com.studyjava.domain.enums.PaymentType;

/**
 * 订单领域事件。使用 sealed interface 限定事件集合，新增事件时编译器会提醒补全 switch 分支。
 */
public sealed interface OrderEvent
    permits OrderEvent.Pay,
        OrderEvent.Prepare,
        OrderEvent.Ship,
        OrderEvent.Complete,
        OrderEvent.ManualClose,
        OrderEvent.TimeoutClose,
        OrderEvent.MerchantClose {

  record Pay(PaymentType paymentType) implements OrderEvent {}

  record Prepare() implements OrderEvent {}

  record Ship() implements OrderEvent {}

  record Complete() implements OrderEvent {}

  record ManualClose() implements OrderEvent {}

  record TimeoutClose() implements OrderEvent {}

  record MerchantClose() implements OrderEvent {}
}
