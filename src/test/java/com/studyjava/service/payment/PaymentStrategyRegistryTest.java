package com.studyjava.service.payment;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.studyjava.domain.enums.PaymentType;

class PaymentStrategyRegistryTest {

  @Test
  void selectsStrategyByPaymentType() {
    PaymentStrategy alipay = strategy(PaymentType.ALIPAY);
    PaymentStrategy wechat = strategy(PaymentType.WECHAT_PAY);
    PaymentStrategyRegistry registry = new PaymentStrategyRegistry(List.of(alipay, wechat));

    assertSame(alipay, registry.get(PaymentType.ALIPAY));
    assertSame(wechat, registry.get(PaymentType.WECHAT_PAY));
  }

  @Test
  void rejectsDuplicateStrategiesDuringStartup() {
    PaymentStrategy first = strategy(PaymentType.ALIPAY);
    PaymentStrategy duplicate = strategy(PaymentType.ALIPAY);

    assertThrows(
        IllegalStateException.class,
        () -> new PaymentStrategyRegistry(List.of(first, duplicate)));
  }

  private PaymentStrategy strategy(PaymentType paymentType) {
    PaymentStrategy strategy = mock(PaymentStrategy.class);
    when(strategy.supportedType()).thenReturn(paymentType);
    return strategy;
  }
}
