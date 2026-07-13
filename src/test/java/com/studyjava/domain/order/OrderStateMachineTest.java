package com.studyjava.domain.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.studyjava.domain.enums.OrderStatus;
import com.studyjava.domain.enums.PaymentType;
import com.studyjava.exception.StudyJavaException;

class OrderStateMachineTest {

  @Test
  void followsCompleteOrderLifecycle() {
    OrderTransition paid =
        OrderStateMachine.transition(
            OrderStatus.PENDING_PAYMENT, new OrderEvent.Pay(PaymentType.ALIPAY));
    OrderTransition prepared =
        OrderStateMachine.transition(paid.to(), new OrderEvent.Prepare());
    OrderTransition shipped = OrderStateMachine.transition(prepared.to(), new OrderEvent.Ship());
    OrderTransition completed =
        OrderStateMachine.transition(shipped.to(), new OrderEvent.Complete());

    assertEquals(OrderStatus.PAID, paid.to());
    assertEquals(1, paid.payStatus());
    assertEquals(PaymentType.ALIPAY.getCode(), paid.payType());
    assertTrue(paid.recordPayTime());
    assertEquals(OrderStatus.PREPARED, prepared.to());
    assertEquals(OrderStatus.SHIPPED, shipped.to());
    assertEquals(OrderStatus.COMPLETED, completed.to());
    assertFalse(completed.recordPayTime());
  }

  @Test
  void closesPendingOrderByTimeout() {
    OrderTransition transition =
        OrderStateMachine.transition(
            OrderStatus.PENDING_PAYMENT, new OrderEvent.TimeoutClose());

    assertEquals(OrderStatus.TIMEOUT_CLOSED, transition.to());
  }

  @Test
  void rejectsSkippingLifecycleState() {
    StudyJavaException exception =
        assertThrows(
            StudyJavaException.class,
            () ->
                OrderStateMachine.transition(
                    OrderStatus.PENDING_PAYMENT, new OrderEvent.Ship()));

    assertEquals("订单状态不能从待支付变更为出库成功", exception.getMessage());
  }

  @Test
  void rejectsChangingTerminalOrder() {
    assertThrows(
        StudyJavaException.class,
        () ->
            OrderStateMachine.transition(
                OrderStatus.COMPLETED, new OrderEvent.MerchantClose()));
  }
}
