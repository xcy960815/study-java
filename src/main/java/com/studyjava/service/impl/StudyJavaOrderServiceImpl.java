package com.studyjava.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.studyjava.domain.dao.StudyJavaGoodsDao;
import com.studyjava.domain.dao.StudyJavaOrderDao;
import com.studyjava.domain.dao.StudyJavaOrderItemDao;
import com.studyjava.domain.dao.StudyJavaOrderPaymentDao;
import com.studyjava.domain.dto.StudyJavaPayOrderDto;
import com.studyjava.domain.dto.StudyJavaPlaceOrderDto;
import com.studyjava.domain.dto.StudyJavaPlaceOrderItemDto;
import com.studyjava.domain.dto.StudyJavaOrderDto;
import com.studyjava.domain.dto.StudyJavaOrderTransitionDto;
import com.studyjava.domain.enums.OrderAction;
import com.studyjava.domain.enums.OrderStatus;
import com.studyjava.domain.enums.PaymentStatus;
import com.studyjava.domain.enums.PaymentType;
import com.studyjava.domain.event.OrderPaidEvent;
import com.studyjava.domain.order.OrderEvent;
import com.studyjava.domain.order.OrderStateMachine;
import com.studyjava.domain.order.OrderTransition;
import com.studyjava.domain.payment.PaymentCommand;
import com.studyjava.domain.payment.PaymentResult;
import com.studyjava.domain.vo.StudyJavaOrderVo;
import com.studyjava.domain.vo.StudyJavaPaymentVo;
import com.studyjava.exception.StudyJavaException;
import com.studyjava.mapper.StudyJavaGoodsMapper;
import com.studyjava.mapper.StudyJavaOrderItemMapper;
import com.studyjava.mapper.StudyJavaOrderMapper;
import com.studyjava.mapper.StudyJavaOrderPaymentMapper;
import com.studyjava.service.OrderNumberGenerator;
import com.studyjava.service.StudyJavaOrderService;
import com.studyjava.service.payment.PaymentStrategy;
import com.studyjava.service.payment.PaymentStrategyRegistry;

import jakarta.annotation.Resource;

/**
 * @author opera
 * @description 针对表【study_java_order】的数据库操作Service实现
 * @createDate 2025-01-18 23:36:52
 */
@Service
public class StudyJavaOrderServiceImpl implements StudyJavaOrderService {
  @Resource private StudyJavaOrderMapper studyJavaOrderMapper;
  @Resource private StudyJavaGoodsMapper studyJavaGoodsMapper;
  @Resource private StudyJavaOrderItemMapper studyJavaOrderItemMapper;
  @Resource private StudyJavaOrderPaymentMapper studyJavaOrderPaymentMapper;
  @Resource private OrderNumberGenerator orderNumberGenerator;
  @Resource private PaymentStrategyRegistry paymentStrategyRegistry;
  @Resource private ApplicationEventPublisher applicationEventPublisher;

  /**
   * 获取订单列表
   *
   * @param page Page<StudyJavaOrderDao>
   * @param studyJavaOrderDto StudyJavaOrderDto
   * @return IPage<StudyJavaOrderVo>
   */
  @Override
  public IPage<StudyJavaOrderVo> getOrderList(
      IPage<StudyJavaOrderDao> page, StudyJavaOrderDto studyJavaOrderDto) {
    StudyJavaOrderDao studyJavaOrderDao = convertToDao(studyJavaOrderDto);
    IPage<StudyJavaOrderDao> studyJavaOrderResponseDao =
        studyJavaOrderMapper.getOrderList(page, studyJavaOrderDao);
    // 转换为 VO 并组装新分页对象
    List<StudyJavaOrderVo> voList =
        studyJavaOrderResponseDao.getRecords().stream()
            .map(this::convertToVo)
            .collect(Collectors.toList());
    IPage<StudyJavaOrderVo> voPage =
        new Page<>(
            studyJavaOrderResponseDao.getCurrent(),
            studyJavaOrderResponseDao.getSize(),
            studyJavaOrderResponseDao.getTotal());
    voPage.setRecords(voList);
    return voPage;
  }

  /** dao 转 vo */
  private StudyJavaOrderVo convertToVo(StudyJavaOrderDao studyJavaOrderDao) {
    if (studyJavaOrderDao == null) {
      return null;
    }
    StudyJavaOrderVo studyJavaOrderVo = new StudyJavaOrderVo();
    BeanUtils.copyProperties(studyJavaOrderDao, studyJavaOrderVo);
    return studyJavaOrderVo;
  }

  /** dto 转 dao */
  private StudyJavaOrderDao convertToDao(StudyJavaOrderDto studyJavaOrderDto) {
    if (studyJavaOrderDto == null) {
      return null;
    }
    StudyJavaOrderDao studyJavaOrderDao = new StudyJavaOrderDao();
    BeanUtils.copyProperties(studyJavaOrderDto, studyJavaOrderDao);
    return studyJavaOrderDao;
  }

  /** 获取订单信息 */
  @Override
  public StudyJavaOrderVo getOrderInfo(StudyJavaOrderDto studyJavaOrderDto) {
    StudyJavaOrderDao studyJavaOrderDao = convertToDao(studyJavaOrderDto);
    StudyJavaOrderDao studyJavaOrderResponseDao =
        studyJavaOrderMapper.getOrderInfo(studyJavaOrderDao);
    return convertToVo(studyJavaOrderResponseDao);
  }

  /** 新建订单 */
  @Override
  public Boolean insertOrder(StudyJavaOrderDto studyJavaOrderDto) {
    StudyJavaOrderDao studyJavaOrderDao = convertToDao(studyJavaOrderDto);
    return studyJavaOrderMapper.insertOrder(studyJavaOrderDao) == 1;
  }

  @Override
  @Transactional
  public StudyJavaOrderVo placeOrder(StudyJavaPlaceOrderDto placeOrderDto) {
    if (placeOrderDto == null) {
      throw new StudyJavaException("下单参数不能为空");
    }
    List<StudyJavaPlaceOrderItemDto> items = validateAndSortItems(placeOrderDto.items());
    Set<Long> goodsIds =
        items.stream().map(StudyJavaPlaceOrderItemDto::goodsId).collect(Collectors.toSet());
    Map<Long, StudyJavaGoodsDao> goodsById =
        studyJavaGoodsMapper.selectBatchIds(goodsIds).stream()
            .collect(Collectors.toMap(StudyJavaGoodsDao::getGoodsId, Function.identity()));

    int totalPrice = calculateTotalPrice(items, goodsById);
    for (StudyJavaPlaceOrderItemDto item : items) {
      if (studyJavaGoodsMapper.decreaseStock(item.goodsId(), item.quantity()) != 1) {
        StudyJavaGoodsDao goods = goodsById.get(item.goodsId());
        throw new StudyJavaException("商品库存不足: " + goods.getGoodsName());
      }
    }

    LocalDateTime now = LocalDateTime.now();
    StudyJavaOrderDao order = createPendingOrder(placeOrderDto, totalPrice, now);
    if (studyJavaOrderMapper.insertOrder(order) != 1 || order.getOrderId() == null) {
      throw new StudyJavaException("订单创建失败");
    }

    List<StudyJavaOrderItemDao> orderItems = createOrderItems(order.getOrderId(), items, goodsById);
    if (studyJavaOrderItemMapper.insertBatch(orderItems) != orderItems.size()) {
      throw new StudyJavaException("订单明细创建失败");
    }
    return convertToVo(order);
  }

  @Override
  @Transactional
  public StudyJavaPaymentVo payOrder(StudyJavaPayOrderDto payOrderDto) {
    if (payOrderDto == null) {
      throw new StudyJavaException("支付参数不能为空");
    }
    if (payOrderDto.requestId() == null
        || payOrderDto.requestId().isBlank()
        || payOrderDto.requestId().length() > 64
        || payOrderDto.orderId() == null
        || payOrderDto.orderId() <= 0) {
      throw new StudyJavaException("支付参数不合法");
    }
    PaymentType paymentType = PaymentType.fromCode(payOrderDto.payType());
    StudyJavaOrderDao order = studyJavaOrderMapper.getOrderByIdForUpdate(payOrderDto.orderId());
    if (order == null) {
      throw new StudyJavaException("订单不存在");
    }

    StudyJavaOrderPaymentDao payment = createPayment(payOrderDto, order, paymentType);
    if (studyJavaOrderPaymentMapper.insertIfAbsent(payment) == 0) {
      StudyJavaOrderPaymentDao existingPayment =
          studyJavaOrderPaymentMapper.getByRequestIdForUpdate(payOrderDto.requestId());
      return resolveIdempotentPayment(payOrderDto, order, existingPayment);
    }
    if (OrderStatus.fromCode(order.getOrderStatus()) != OrderStatus.PENDING_PAYMENT) {
      throw new StudyJavaException("当前订单状态不能支付");
    }

    PaymentStrategy paymentStrategy = paymentStrategyRegistry.get(paymentType);
    PaymentResult paymentResult =
        paymentStrategy.pay(
            new PaymentCommand(payOrderDto.requestId(), order.getOrderNo(), order.getTotalPrice()));
    if (!paymentResult.successful()) {
      throw new StudyJavaException("支付失败: " + paymentResult.message());
    }

    StudyJavaOrderVo paidOrder =
        applyTransition(
            new StudyJavaOrderTransitionDto(
                order.getOrderId(), OrderAction.PAY, paymentType.getCode()));
    if (studyJavaOrderPaymentMapper.markSuccess(
            payOrderDto.requestId(), paymentResult.transactionNo())
        != 1) {
      throw new StudyJavaException("支付流水更新失败");
    }

    applicationEventPublisher.publishEvent(
        new OrderPaidEvent(
            paidOrder.getOrderId(),
            paidOrder.getOrderNo(),
            paidOrder.getUserId(),
            paidOrder.getTotalPrice(),
            paymentType.getCode(),
            paymentResult.transactionNo()));
    return toPaymentVo(paidOrder, paymentResult.transactionNo(), false);
  }

  private StudyJavaOrderPaymentDao createPayment(
      StudyJavaPayOrderDto payOrderDto, StudyJavaOrderDao order, PaymentType paymentType) {
    StudyJavaOrderPaymentDao payment = new StudyJavaOrderPaymentDao();
    payment.setRequestId(payOrderDto.requestId());
    payment.setOrderId(order.getOrderId());
    payment.setPaymentType(paymentType.getCode());
    payment.setPaymentStatus(PaymentStatus.PROCESSING.getCode());
    payment.setAmount(order.getTotalPrice());
    payment.setIsDeleted(0);
    return payment;
  }

  private StudyJavaPaymentVo resolveIdempotentPayment(
      StudyJavaPayOrderDto request,
      StudyJavaOrderDao order,
      StudyJavaOrderPaymentDao existingPayment) {
    if (existingPayment == null) {
      throw new StudyJavaException("支付幂等记录读取失败，请稍后重试");
    }
    if (!request.orderId().equals(existingPayment.getOrderId())
        || !request.payType().equals(existingPayment.getPaymentType())) {
      throw new StudyJavaException("支付幂等键已被其他订单或支付方式使用");
    }
    if (!Integer.valueOf(PaymentStatus.SUCCESS.getCode())
        .equals(existingPayment.getPaymentStatus())) {
      throw new StudyJavaException("支付正在处理中，请稍后查询");
    }
    if (OrderStatus.fromCode(order.getOrderStatus()) != OrderStatus.PAID) {
      throw new StudyJavaException("支付流水与订单状态不一致");
    }
    return toPaymentVo(convertToVo(order), existingPayment.getTransactionNo(), true);
  }

  private StudyJavaPaymentVo toPaymentVo(
      StudyJavaOrderVo order, String transactionNo, boolean idempotent) {
    return new StudyJavaPaymentVo(
        order.getOrderId(),
        order.getOrderNo(),
        order.getTotalPrice(),
        order.getOrderStatus(),
        order.getPayStatus(),
        order.getPayType(),
        transactionNo,
        idempotent);
  }

  private List<StudyJavaPlaceOrderItemDto> validateAndSortItems(
      List<StudyJavaPlaceOrderItemDto> requestedItems) {
    if (requestedItems == null || requestedItems.isEmpty()) {
      throw new StudyJavaException("订单至少包含一个商品");
    }
    if (requestedItems.size() > 50) {
      throw new StudyJavaException("一个订单最多包含50种商品");
    }
    Set<Long> goodsIds = new HashSet<>();
    for (StudyJavaPlaceOrderItemDto item : requestedItems) {
      if (item == null
          || item.goodsId() == null
          || item.quantity() == null
          || item.quantity() <= 0
          || item.quantity() > 999) {
        throw new StudyJavaException("订单商品参数不合法");
      }
      if (!goodsIds.add(item.goodsId())) {
        throw new StudyJavaException("订单中不能重复添加同一商品: " + item.goodsId());
      }
    }
    return requestedItems.stream()
        .sorted(Comparator.comparing(StudyJavaPlaceOrderItemDto::goodsId))
        .toList();
  }

  private int calculateTotalPrice(
      List<StudyJavaPlaceOrderItemDto> items, Map<Long, StudyJavaGoodsDao> goodsById) {
    int totalPrice = 0;
    try {
      for (StudyJavaPlaceOrderItemDto item : items) {
        StudyJavaGoodsDao goods = goodsById.get(item.goodsId());
        if (goods == null) {
          throw new StudyJavaException("商品不存在: " + item.goodsId());
        }
        if (!Integer.valueOf(1).equals(goods.getGoodsSellStatus())) {
          throw new StudyJavaException("商品已下架: " + goods.getGoodsName());
        }
        if (goods.getSellingPrice() == null || goods.getSellingPrice() <= 0) {
          throw new StudyJavaException("商品价格不合法: " + goods.getGoodsName());
        }
        int linePrice = Math.multiplyExact(goods.getSellingPrice(), item.quantity());
        totalPrice = Math.addExact(totalPrice, linePrice);
      }
      return totalPrice;
    } catch (ArithmeticException exception) {
      throw new StudyJavaException("订单金额超出允许范围");
    }
  }

  private StudyJavaOrderDao createPendingOrder(
      StudyJavaPlaceOrderDto placeOrderDto, int totalPrice, LocalDateTime now) {
    StudyJavaOrderDao order = new StudyJavaOrderDao();
    order.setOrderNo(orderNumberGenerator.nextOrderNumber());
    order.setUserId(placeOrderDto.userId());
    order.setTotalPrice(totalPrice);
    order.setPayStatus(0);
    order.setPayType(0);
    order.setOrderStatus(OrderStatus.PENDING_PAYMENT.getCode());
    order.setExtraInfo("");
    order.setUserName(placeOrderDto.userName());
    order.setUserPhone(placeOrderDto.userPhone());
    order.setUserAddress(placeOrderDto.userAddress());
    order.setCreateTime(now);
    order.setUpdateTime(now);
    order.setIsDeleted(0);
    return order;
  }

  private List<StudyJavaOrderItemDao> createOrderItems(
      Long orderId,
      List<StudyJavaPlaceOrderItemDto> items,
      Map<Long, StudyJavaGoodsDao> goodsById) {
    List<StudyJavaOrderItemDao> orderItems = new ArrayList<>(items.size());
    for (StudyJavaPlaceOrderItemDto item : items) {
      StudyJavaGoodsDao goods = goodsById.get(item.goodsId());
      StudyJavaOrderItemDao orderItem = new StudyJavaOrderItemDao();
      orderItem.setOrderId(orderId);
      orderItem.setGoodsId(goods.getGoodsId());
      orderItem.setGoodsName(goods.getGoodsName());
      orderItem.setGoodsCoverImg(goods.getGoodsCoverImg());
      orderItem.setSellingPrice(goods.getSellingPrice());
      orderItem.setGoodsCount(item.quantity());
      orderItems.add(orderItem);
    }
    return orderItems;
  }

  /** 更新订单 */
  @Override
  public Boolean updateOrder(StudyJavaOrderDto studyJavaOrderDto) {
    if (studyJavaOrderDto == null || studyJavaOrderDto.getOrderId() == null) {
      throw new StudyJavaException("订单ID不能为空");
    }
    if (studyJavaOrderDto.getOrderStatus() != null
        || studyJavaOrderDto.getPayStatus() != null
        || studyJavaOrderDto.getPayType() != null
        || studyJavaOrderDto.getPayTime() != null
        || studyJavaOrderDto.getOrderNo() != null
        || studyJavaOrderDto.getUserId() != null
        || studyJavaOrderDto.getTotalPrice() != null) {
      throw new StudyJavaException("订单核心字段必须通过下单、状态流转或支付接口修改");
    }
    StudyJavaOrderDao studyJavaOrderDao = convertToDao(studyJavaOrderDto);
    return studyJavaOrderMapper.updateOrder(studyJavaOrderDao);
  }

  @Override
  @Transactional
  public StudyJavaOrderVo transitionOrder(StudyJavaOrderTransitionDto transitionDto) {
    if (transitionDto.action() == OrderAction.PAY) {
      throw new StudyJavaException("支付订单请使用 /order/pay 接口");
    }
    return applyTransition(transitionDto);
  }

  private StudyJavaOrderVo applyTransition(StudyJavaOrderTransitionDto transitionDto) {
    StudyJavaOrderDto query = new StudyJavaOrderDto();
    query.setOrderId(transitionDto.orderId());
    StudyJavaOrderDao order = studyJavaOrderMapper.getOrderInfo(convertToDao(query));
    if (order == null) {
      throw new StudyJavaException("订单不存在");
    }

    OrderStatus currentStatus = OrderStatus.fromCode(order.getOrderStatus());
    OrderTransition transition =
        OrderStateMachine.transition(currentStatus, createEvent(transitionDto));
    Date payTime = transition.recordPayTime() ? new Date() : null;

    int affectedRows =
        studyJavaOrderMapper.transitionOrder(
            transitionDto.orderId(),
            transition.from().getCode(),
            transition.to().getCode(),
            transition.payStatus(),
            transition.payType(),
            payTime);
    if (affectedRows != 1) {
      throw new StudyJavaException("订单状态已发生变化，请刷新后重试");
    }

    StudyJavaOrderDao updatedOrder = studyJavaOrderMapper.getOrderInfo(convertToDao(query));
    return convertToVo(updatedOrder);
  }

  private OrderEvent createEvent(StudyJavaOrderTransitionDto transitionDto) {
    return switch (transitionDto.action()) {
      case PAY -> new OrderEvent.Pay(PaymentType.fromCode(transitionDto.payType()));
      case PREPARE -> new OrderEvent.Prepare();
      case SHIP -> new OrderEvent.Ship();
      case COMPLETE -> new OrderEvent.Complete();
      case MANUAL_CLOSE -> new OrderEvent.ManualClose();
      case TIMEOUT_CLOSE -> new OrderEvent.TimeoutClose();
      case MERCHANT_CLOSE -> new OrderEvent.MerchantClose();
    };
  }

  /** 删除订单 */
  @Override
  public Boolean deleteOrder(StudyJavaOrderDto studyJavaOrderDto) {
    StudyJavaOrderDao studyJavaOrderDao = convertToDao(studyJavaOrderDto);
    return studyJavaOrderMapper.deleteOrder(studyJavaOrderDao);
  }

  @Override
  public Map<String, Object> getDailyStats(LocalDate date) {
    LocalDateTime startOfDay = date.atStartOfDay();
    LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    return studyJavaOrderMapper.getDailyStats(
        startOfDay.format(formatter), endOfDay.format(formatter));
  }
}
