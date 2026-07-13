package com.studyjava.service.payment;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.studyjava.domain.enums.PaymentType;
import com.studyjava.exception.StudyJavaException;

/** 将 Spring 发现的支付策略注册为不可变映射。 */
@Component
public class PaymentStrategyRegistry {

  private final Map<PaymentType, PaymentStrategy> strategies;

  public PaymentStrategyRegistry(List<PaymentStrategy> paymentStrategies) {
    EnumMap<PaymentType, PaymentStrategy> registry = new EnumMap<>(PaymentType.class);
    for (PaymentStrategy strategy : paymentStrategies) {
      PaymentStrategy previous = registry.put(strategy.supportedType(), strategy);
      if (previous != null) {
        throw new IllegalStateException("重复的支付策略: " + strategy.supportedType());
      }
    }
    this.strategies = Map.copyOf(registry);
  }

  public PaymentStrategy get(PaymentType paymentType) {
    PaymentStrategy strategy = strategies.get(paymentType);
    if (strategy == null) {
      throw new StudyJavaException("未配置支付策略: " + paymentType.getMessage());
    }
    return strategy;
  }
}
