package com.studyjava.domain.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/** 下单商品项。 */
public record StudyJavaPlaceOrderItemDto(
    @NotNull(message = "商品ID不能为空") @Positive(message = "商品ID必须大于0") Long goodsId,
    @NotNull(message = "购买数量不能为空")
        @Min(value = 1, message = "购买数量至少为1")
        @Max(value = 999, message = "单个商品购买数量不能超过999")
        Integer quantity) {}
