package com.studyjava.domain.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/** 创建订单请求。使用不可变 record 防止业务执行过程中请求数据被修改。 */
public record StudyJavaPlaceOrderDto(
    @NotNull(message = "用户ID不能为空") @Positive(message = "用户ID必须大于0") Long userId,
    @NotBlank(message = "收货人不能为空") @Size(max = 30, message = "收货人不能超过30个字符") String userName,
    @NotBlank(message = "手机号不能为空")
        @Pattern(regexp = "\\d{11}", message = "手机号必须是11位数字")
        String userPhone,
    @NotBlank(message = "收货地址不能为空") @Size(max = 100, message = "收货地址不能超过100个字符") String userAddress,
    @Valid
        @NotEmpty(message = "订单至少包含一个商品")
        @Size(max = 50, message = "一个订单最多包含50种商品")
        List<StudyJavaPlaceOrderItemDto> items) {

  public StudyJavaPlaceOrderDto {
    if (items != null) {
      items = List.copyOf(items);
    }
  }
}
