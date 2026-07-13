package com.studyjava.component;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.studyjava.domain.event.OrderPaidEvent;

class OrderPaidNotificationListenerTest {

  @Test
  void sendsUserNotificationAfterTransactionCommit() throws Exception {
    SimpMessagingTemplate messagingTemplate = mock(SimpMessagingTemplate.class);
    OrderPaidNotificationListener listener =
        new OrderPaidNotificationListener(messagingTemplate);
    OrderPaidEvent event = new OrderPaidEvent(1L, "ORDER-1", 7L, 500, 2, "WX-1");

    listener.onOrderPaid(event);

    verify(messagingTemplate).convertAndSend("/topic/orders/7", event);
    Method listenerMethod =
        OrderPaidNotificationListener.class.getMethod("onOrderPaid", OrderPaidEvent.class);
    TransactionalEventListener annotation =
        listenerMethod.getAnnotation(TransactionalEventListener.class);
    assertNotNull(annotation);
    assertEquals(TransactionPhase.AFTER_COMMIT, annotation.phase());
  }
}
