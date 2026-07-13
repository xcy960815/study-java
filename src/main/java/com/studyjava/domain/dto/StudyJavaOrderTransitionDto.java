package com.studyjava.domain.dto;

import com.studyjava.domain.enums.OrderAction;

import jakarta.validation.constraints.NotNull;

/** 订单状态流转请求。record 适合表达只承载数据的不可变请求对象。 */
public record StudyJavaOrderTransitionDto(
    @NotNull(message = "订单ID不能为空") Long orderId,
    @NotNull(message = "订单动作不能为空") OrderAction action,
    Integer payType) {}
