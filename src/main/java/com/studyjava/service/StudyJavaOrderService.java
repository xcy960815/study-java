package com.studyjava.service;

import java.time.LocalDate;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.studyjava.domain.dao.StudyJavaOrderDao;
import com.studyjava.domain.dto.StudyJavaPayOrderDto;
import com.studyjava.domain.dto.StudyJavaPlaceOrderDto;
import com.studyjava.domain.dto.StudyJavaOrderDto;
import com.studyjava.domain.dto.StudyJavaOrderTransitionDto;
import com.studyjava.domain.vo.StudyJavaOrderVo;
import com.studyjava.domain.vo.StudyJavaPaymentVo;

/**
 * @author opera
 * @description 针对表【study_java_order】的数据库操作Service
 * @createDate 2025-01-18 23:36:52
 */
public interface StudyJavaOrderService {
  /**
   * 获取订单列表
   *
   * @param page Page<StudyJavaSysUserDto>
   * @param studyJavaOrderDto StudyJavaOrderDto
   * @return IPage<StudyJavaOrderVo>
   */
  IPage<StudyJavaOrderVo> getOrderList(
      IPage<StudyJavaOrderDao> page, StudyJavaOrderDto studyJavaOrderDto);

  /** 获取订单信息 */
  StudyJavaOrderVo getOrderInfo(StudyJavaOrderDto studyJavaOrderDto);

  /** 新建订单 */
  Boolean insertOrder(StudyJavaOrderDto studyJavaOrderDto);

  /** 按数据库商品价格创建订单并原子扣减库存。 */
  StudyJavaOrderVo placeOrder(StudyJavaPlaceOrderDto placeOrderDto);

  /** 使用支付策略和幂等键支付订单。 */
  StudyJavaPaymentVo payOrder(StudyJavaPayOrderDto payOrderDto);

  /** 更新订单 */
  Boolean updateOrder(StudyJavaOrderDto studyJavaOrderDto);

  /** 按状态机规则推进订单状态。 */
  StudyJavaOrderVo transitionOrder(StudyJavaOrderTransitionDto transitionDto);

  /** 删除订单 */
  Boolean deleteOrder(StudyJavaOrderDto studyJavaOrderDto);

  /** 获取每日统计 */
  Map<String, Object> getDailyStats(LocalDate date);
}
