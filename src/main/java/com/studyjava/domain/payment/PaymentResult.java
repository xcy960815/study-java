package com.studyjava.domain.payment;

/** 支付渠道返回的统一结果。 */
public record PaymentResult(boolean successful, String transactionNo, String message) {

  public static PaymentResult success(String transactionNo) {
    return new PaymentResult(true, transactionNo, "支付成功");
  }

  public static PaymentResult failure(String message) {
    return new PaymentResult(false, null, message);
  }
}
