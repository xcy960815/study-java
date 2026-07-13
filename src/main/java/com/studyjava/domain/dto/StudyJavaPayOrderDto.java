package com.studyjava.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/** 支付订单请求。requestId 由调用方生成，用于安全重试。 */
public record StudyJavaPayOrderDto(
    @NotBlank(message = "支付幂等键不能为空")
        @Size(max = 64, message = "支付幂等键不能超过64个字符")
        String requestId,
    @NotNull(message = "订单ID不能为空") @Positive(message = "订单ID必须大于0") Long orderId,
    @NotNull(message = "支付方式不能为空") Integer payType) {}
