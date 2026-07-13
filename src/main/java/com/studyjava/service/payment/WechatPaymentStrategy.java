package com.studyjava.service.payment;

import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.studyjava.domain.enums.PaymentType;
import com.studyjava.domain.payment.PaymentCommand;
import com.studyjava.domain.payment.PaymentResult;

/** 微信支付模拟策略。后续可在此处替换成微信支付 SDK。 */
@Component
public class WechatPaymentStrategy implements PaymentStrategy {

  @Override
  public PaymentType supportedType() {
    return PaymentType.WECHAT_PAY;
  }

  @Override
  public PaymentResult pay(PaymentCommand command) {
    return PaymentResult.success("WX-" + IdWorker.getIdStr());
  }
}
