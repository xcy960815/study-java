package com.studyjava.component;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.studyjava.domain.event.OrderPaidEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/** 只在支付事务提交后通知客户端，避免前端收到最终被回滚的订单消息。 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderPaidNotificationListener {

  private final SimpMessagingTemplate messagingTemplate;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void onOrderPaid(OrderPaidEvent event) {
    String destination = "/topic/orders/" + event.userId();
    messagingTemplate.convertAndSend(destination, event);
    log.info("订单支付通知已发送: orderNo={}, destination={}", event.orderNo(), destination);
  }
}
