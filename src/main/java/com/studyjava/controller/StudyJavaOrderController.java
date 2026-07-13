package com.studyjava.controller;

import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.studyjava.annotation.Log;
import com.studyjava.domain.dto.StudyJavaPayOrderDto;
import com.studyjava.domain.dto.StudyJavaPlaceOrderDto;
import com.studyjava.domain.dto.StudyJavaOrderDto;
import com.studyjava.domain.dto.StudyJavaOrderTransitionDto;
import com.studyjava.domain.enums.BusinessType;
import com.studyjava.domain.vo.StudyJavaOrderVo;
import com.studyjava.domain.vo.StudyJavaPaymentVo;
import com.studyjava.service.StudyJavaOrderService;
import com.studyjava.utils.PageResult;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/** 订单控制器 */
@Slf4j
@RestController
@RequestMapping("/order")
public class StudyJavaOrderController extends BaseController {

  @Resource private StudyJavaOrderService studyJavaOrderService;

  /**
   * 获取用户订单列表
   *
   * @param pageNum 页码
   * @param pageSize 每页大小
   * @return PageResult<StudyJavaOrderVo>
   */
  @com.studyjava.annotation.PreAuthorize("order:query")
  @GetMapping("/getOrderList")
  public PageResult<StudyJavaOrderVo> getOrderList(
      @RequestParam(defaultValue = "1") Integer pageNum,
      @RequestParam(defaultValue = "10") Integer pageSize,
      @ModelAttribute StudyJavaOrderDto studyJavaOrderDto) {
    IPage<StudyJavaOrderVo> orderVoPage =
        studyJavaOrderService.getOrderList(startPage(pageNum, pageSize), studyJavaOrderDto);
    return PageResult.of(orderVoPage.getRecords(), orderVoPage.getTotal());
  }

  /**
   * 获取订单详情
   *
   * @param orderId 订单ID
   * @return StudyJavaOrderVo
   */
  @com.studyjava.annotation.PreAuthorize("order:query")
  @GetMapping("/getOrderInfo")
  public StudyJavaOrderVo getOrderInfo(@RequestParam("id") Long orderId) {
    StudyJavaOrderDto orderDto = new StudyJavaOrderDto();
    orderDto.setOrderId(orderId);
    return studyJavaOrderService.getOrderInfo(orderDto);
  }

  /** 新建订单 */
  @com.studyjava.annotation.PreAuthorize("order:add")
  @PostMapping("/insertOrder")
  public Boolean insertOrder(@Valid @RequestBody StudyJavaOrderDto orderDto) {
    return studyJavaOrderService.insertOrder(orderDto);
  }

  /** 创建订单并扣减库存。订单价格以数据库中的商品售价为准。 */
  @Log(title = "订单管理", businessType = BusinessType.INSERT)
  @com.studyjava.annotation.PreAuthorize("order:add")
  @PostMapping("/place")
  public StudyJavaOrderVo placeOrder(@Valid @RequestBody StudyJavaPlaceOrderDto placeOrderDto) {
    return studyJavaOrderService.placeOrder(placeOrderDto);
  }

  /** 幂等支付订单。相同 requestId 的重复请求会返回首次成功结果。 */
  @Log(title = "订单支付", businessType = BusinessType.UPDATE)
  @com.studyjava.annotation.PreAuthorize("order:edit")
  @PostMapping("/pay")
  public StudyJavaPaymentVo payOrder(@Valid @RequestBody StudyJavaPayOrderDto payOrderDto) {
    return studyJavaOrderService.payOrder(payOrderDto);
  }

  /**
   * 更新订单状态
   *
   * @param orderDto 订单DTO
   * @return Boolean
   */
  @com.studyjava.annotation.PreAuthorize("order:edit")
  @PutMapping("/updateOrder")
  public Boolean updateOrder(@Valid @RequestBody StudyJavaOrderDto orderDto) {
    return studyJavaOrderService.updateOrder(orderDto);
  }

  /** 按订单状态机执行支付、配货、发货、完成或关闭动作。 */
  @com.studyjava.annotation.PreAuthorize("order:edit")
  @PostMapping("/transition")
  public StudyJavaOrderVo transitionOrder(
      @Valid @RequestBody StudyJavaOrderTransitionDto transitionDto) {
    return studyJavaOrderService.transitionOrder(transitionDto);
  }

  /**
   * 删除订单（逻辑删除）
   *
   * @param orderId 订单ID
   * @return Boolean
   */
  @com.studyjava.annotation.PreAuthorize("order:remove")
  @DeleteMapping("/deleteOrder")
  public Boolean deleteOrder(@RequestParam("id") Long orderId) {
    StudyJavaOrderDto orderDto = new StudyJavaOrderDto();
    orderDto.setOrderId(orderId);
    return studyJavaOrderService.deleteOrder(orderDto);
  }
}
