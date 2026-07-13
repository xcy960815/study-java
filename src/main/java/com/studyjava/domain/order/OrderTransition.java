package com.studyjava.domain.order;

import com.studyjava.domain.enums.OrderStatus;

/** 一次订单状态流转的不可变结果。 */
public record OrderTransition(
    OrderStatus from,
    OrderStatus to,
    Integer payStatus,
    Integer payType,
    boolean recordPayTime) {}
