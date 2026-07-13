package com.studyjava.domain.enums;

/** 可以触发的订单动作。 */
public enum OrderAction {
  PAY,
  PREPARE,
  SHIP,
  COMPLETE,
  MANUAL_CLOSE,
  TIMEOUT_CLOSE,
  MERCHANT_CLOSE
}
