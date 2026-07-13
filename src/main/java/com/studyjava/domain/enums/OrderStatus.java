package com.studyjava.domain.enums;

import java.util.Arrays;

import com.studyjava.exception.StudyJavaException;

/** 订单状态。 */
public enum OrderStatus implements BaseEnum {
  PENDING_PAYMENT(0, "待支付"),
  PAID(1, "已支付"),
  PREPARED(2, "配货完成"),
  SHIPPED(3, "出库成功"),
  COMPLETED(4, "交易成功"),
  MANUALLY_CLOSED(-1, "手动关闭"),
  TIMEOUT_CLOSED(-2, "超时关闭"),
  MERCHANT_CLOSED(-3, "商家关闭");

  private final int code;
  private final String message;

  OrderStatus(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public static OrderStatus fromCode(Integer code) {
    if (code == null) {
      throw new StudyJavaException("订单状态不能为空");
    }
    return Arrays.stream(values())
        .filter(status -> status.code == code)
        .findFirst()
        .orElseThrow(() -> new StudyJavaException("未知的订单状态: " + code));
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
