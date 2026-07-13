package com.studyjava.domain.enums;

/** 支付流水状态。失败的模拟支付会回滚，允许使用同一幂等键重试。 */
public enum PaymentStatus implements BaseEnum {
  PROCESSING(0, "处理中"),
  SUCCESS(1, "支付成功");

  private final int code;
  private final String message;

  PaymentStatus(int code, String message) {
    this.code = code;
    this.message = message;
  }

  @Override
  public int getCode() {
    return code;
  }

  @Override
  public String getMessage() {
    return message;
  }
}
