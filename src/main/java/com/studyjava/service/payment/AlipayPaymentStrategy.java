package com.studyjava.service.payment;

import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.studyjava.domain.enums.PaymentType;
import com.studyjava.domain.payment.PaymentCommand;
import com.studyjava.domain.payment.PaymentResult;

/** 支付宝模拟策略。后续可在此处替换成支付宝 SDK。 */
@Component
public class AlipayPaymentStrategy implements PaymentStrategy {

  @Override
  public PaymentType supportedType() {
    return PaymentType.ALIPAY;
  }

  @Override
  public PaymentResult pay(PaymentCommand command) {
    return PaymentResult.success("ALI-" + IdWorker.getIdStr());
  }
}
