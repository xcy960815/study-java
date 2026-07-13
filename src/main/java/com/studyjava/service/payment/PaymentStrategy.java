package com.studyjava.service.payment;

import com.studyjava.domain.enums.PaymentType;
import com.studyjava.domain.payment.PaymentCommand;
import com.studyjava.domain.payment.PaymentResult;

/** 支付渠道策略。接入真实 SDK 时只需要新增实现类。 */
public interface PaymentStrategy {

  PaymentType supportedType();

  PaymentResult pay(PaymentCommand command);
}
