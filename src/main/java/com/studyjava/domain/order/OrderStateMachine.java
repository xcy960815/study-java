package com.studyjava.domain.order;

import com.studyjava.domain.enums.OrderStatus;
import com.studyjava.exception.StudyJavaException;

/** 订单状态机，集中维护合法状态流转规则。 */
public final class OrderStateMachine {

  private OrderStateMachine() {}

  public static OrderTransition transition(OrderStatus current, OrderEvent event) {
    if (current == null || event == null) {
      throw new StudyJavaException("订单当前状态和订单事件不能为空");
    }

    return switch (event) {
      case OrderEvent.Pay pay ->
          move(
              current,
              OrderStatus.PENDING_PAYMENT,
              OrderStatus.PAID,
              1,
              pay.paymentType().getCode(),
              true);
      case OrderEvent.Prepare ignored ->
          move(current, OrderStatus.PAID, OrderStatus.PREPARED, null, null, false);
      case OrderEvent.Ship ignored ->
          move(current, OrderStatus.PREPARED, OrderStatus.SHIPPED, null, null, false);
      case OrderEvent.Complete ignored ->
          move(current, OrderStatus.SHIPPED, OrderStatus.COMPLETED, null, null, false);
      case OrderEvent.ManualClose ignored ->
          move(
              current,
              OrderStatus.PENDING_PAYMENT,
              OrderStatus.MANUALLY_CLOSED,
              null,
              null,
              false);
      case OrderEvent.TimeoutClose ignored ->
          move(
              current,
              OrderStatus.PENDING_PAYMENT,
              OrderStatus.TIMEOUT_CLOSED,
              null,
              null,
              false);
      case OrderEvent.MerchantClose ignored ->
          move(
              current,
              OrderStatus.PENDING_PAYMENT,
              OrderStatus.MERCHANT_CLOSED,
              null,
              null,
              false);
    };
  }

  private static OrderTransition move(
      OrderStatus current,
      OrderStatus expected,
      OrderStatus target,
      Integer payStatus,
      Integer payType,
      boolean recordPayTime) {
    if (current != expected) {
      throw new StudyJavaException(
          "订单状态不能从" + current.getMessage() + "变更为" + target.getMessage());
    }
    return new OrderTransition(current, target, payStatus, payType, recordPayTime);
  }
}
