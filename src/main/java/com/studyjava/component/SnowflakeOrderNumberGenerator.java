package com.studyjava.component;

import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.studyjava.service.OrderNumberGenerator;

/** 使用 MyBatis Plus 的雪花 ID 生成不超过20位的订单号。 */
@Component
public class SnowflakeOrderNumberGenerator implements OrderNumberGenerator {

  @Override
  public String nextOrderNumber() {
    return IdWorker.getIdStr();
  }
}
