package com.studyjava.domain.enums;

import java.util.Arrays;

import com.studyjava.exception.StudyJavaException;

/** 支付方式。 */
public enum PaymentType implements BaseEnum {
  ALIPAY(1, "支付宝"),
  WECHAT_PAY(2, "微信支付");

  private final int code;
  private final String message;

  PaymentType(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public static PaymentType fromCode(Integer code) {
    if (code == null) {
      throw new StudyJavaException("支付订单时必须指定支付方式");
    }
    return Arrays.stream(values())
        .filter(type -> type.code == code)
        .findFirst()
        .orElseThrow(() -> new StudyJavaException("不支持的支付方式: " + code));
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
