package com.studyjava.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

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
import com.studyjava.domain.payment.PaymentCommand;
import com.studyjava.domain.payment.PaymentResult;
import com.studyjava.domain.vo.StudyJavaOrderVo;
import com.studyjava.domain.vo.StudyJavaPaymentVo;
import com.studyjava.exception.StudyJavaException;
import com.studyjava.mapper.StudyJavaGoodsMapper;
import com.studyjava.mapper.StudyJavaOrderItemMapper;
import com.studyjava.mapper.StudyJavaOrderMapper;
import com.studyjava.mapper.StudyJavaOrderPaymentMapper;
import com.studyjava.service.impl.StudyJavaOrderServiceImpl;
import com.studyjava.service.payment.PaymentStrategy;
import com.studyjava.service.payment.PaymentStrategyRegistry;

@ExtendWith(MockitoExtension.class)
class StudyJavaOrderServiceImplTest {

  @Mock private StudyJavaOrderMapper orderMapper;
  @Mock private StudyJavaGoodsMapper goodsMapper;
  @Mock private StudyJavaOrderItemMapper orderItemMapper;
  @Mock private StudyJavaOrderPaymentMapper orderPaymentMapper;
  @Mock private OrderNumberGenerator orderNumberGenerator;
  @Mock private PaymentStrategyRegistry paymentStrategyRegistry;
  @Mock private PaymentStrategy paymentStrategy;
  @Mock private ApplicationEventPublisher eventPublisher;

  @InjectMocks private StudyJavaOrderServiceImpl orderService;

  @Test
  void paysWithSelectedStrategyAndPublishesDomainEvent() {
    StudyJavaOrderDao pendingOrder = order(10L, OrderStatus.PENDING_PAYMENT);
    pendingOrder.setOrderNo("ORDER-10");
    pendingOrder.setUserId(7L);
    pendingOrder.setTotalPrice(500);
    StudyJavaOrderDao paidOrder = order(10L, OrderStatus.PAID);
    paidOrder.setOrderNo("ORDER-10");
    paidOrder.setUserId(7L);
    paidOrder.setTotalPrice(500);
    paidOrder.setPayStatus(1);
    paidOrder.setPayType(PaymentType.WECHAT_PAY.getCode());

    when(orderMapper.getOrderByIdForUpdate(10L)).thenReturn(pendingOrder);
    when(orderPaymentMapper.insertIfAbsent(any())).thenReturn(1);
    when(paymentStrategyRegistry.get(PaymentType.WECHAT_PAY)).thenReturn(paymentStrategy);
    when(paymentStrategy.pay(any(PaymentCommand.class)))
        .thenReturn(PaymentResult.success("WX-TRANSACTION-1"));
    when(orderMapper.getOrderInfo(any())).thenReturn(pendingOrder, paidOrder);
    when(orderMapper.transitionOrder(
            eq(10L),
            eq(OrderStatus.PENDING_PAYMENT.getCode()),
            eq(OrderStatus.PAID.getCode()),
            eq(1),
            eq(PaymentType.WECHAT_PAY.getCode()),
            any(Date.class)))
        .thenReturn(1);
    when(orderPaymentMapper.markSuccess("pay-request-1", "WX-TRANSACTION-1")).thenReturn(1);

    StudyJavaPaymentVo result =
        orderService.payOrder(
            new StudyJavaPayOrderDto("pay-request-1", 10L, PaymentType.WECHAT_PAY.getCode()));

    assertEquals("WX-TRANSACTION-1", result.transactionNo());
    assertFalse(result.idempotent());
    verify(paymentStrategy)
        .pay(
            argThat(
                command ->
                    command.orderNo().equals("ORDER-10") && command.amount().equals(500)));
    verify(eventPublisher)
        .publishEvent(
            argThat(
                event ->
                    event instanceof OrderPaidEvent paidEvent
                        && paidEvent.orderId().equals(10L)
                        && paidEvent.transactionNo().equals("WX-TRANSACTION-1")));
  }

  @Test
  void returnsPreviousResultForRepeatedPaymentRequest() {
    StudyJavaOrderDao paidOrder = order(10L, OrderStatus.PAID);
    paidOrder.setOrderNo("ORDER-10");
    paidOrder.setTotalPrice(500);
    paidOrder.setPayStatus(1);
    paidOrder.setPayType(PaymentType.ALIPAY.getCode());
    StudyJavaOrderPaymentDao existingPayment = new StudyJavaOrderPaymentDao();
    existingPayment.setRequestId("same-request");
    existingPayment.setOrderId(10L);
    existingPayment.setPaymentType(PaymentType.ALIPAY.getCode());
    existingPayment.setPaymentStatus(PaymentStatus.SUCCESS.getCode());
    existingPayment.setTransactionNo("ALI-EXISTING");
    when(orderMapper.getOrderByIdForUpdate(10L)).thenReturn(paidOrder);
    when(orderPaymentMapper.insertIfAbsent(any())).thenReturn(0);
    when(orderPaymentMapper.getByRequestIdForUpdate("same-request"))
        .thenReturn(existingPayment);

    StudyJavaPaymentVo result =
        orderService.payOrder(
            new StudyJavaPayOrderDto("same-request", 10L, PaymentType.ALIPAY.getCode()));

    assertEquals("ALI-EXISTING", result.transactionNo());
    assertTrue(result.idempotent());
    verifyNoInteractions(paymentStrategyRegistry, paymentStrategy, eventPublisher);
  }

  @Test
  void placesOrderUsingDatabasePricesAndGoodsIdLockOrder() {
    StudyJavaGoodsDao firstGoods = goods(1L, "商品A", 120);
    StudyJavaGoodsDao secondGoods = goods(2L, "商品B", 80);
    when(goodsMapper.selectBatchIds(anyCollection())).thenReturn(List.of(firstGoods, secondGoods));
    when(goodsMapper.decreaseStock(any(), any())).thenReturn(1);
    when(orderNumberGenerator.nextOrderNumber()).thenReturn("1000000000000000001");
    when(orderMapper.insertOrder(any()))
        .thenAnswer(
            invocation -> {
              StudyJavaOrderDao order = invocation.getArgument(0);
              order.setOrderId(99L);
              return 1;
            });
    when(orderItemMapper.insertBatch(anyList())).thenReturn(2);

    StudyJavaOrderVo result =
        orderService.placeOrder(
            placeOrder(
                List.of(
                    new StudyJavaPlaceOrderItemDto(2L, 1),
                    new StudyJavaPlaceOrderItemDto(1L, 2))));

    assertEquals(99L, result.getOrderId());
    assertEquals(320, result.getTotalPrice());
    assertEquals(OrderStatus.PENDING_PAYMENT.getCode(), result.getOrderStatus());
    InOrder stockOrder = inOrder(goodsMapper);
    stockOrder.verify(goodsMapper).decreaseStock(1L, 2);
    stockOrder.verify(goodsMapper).decreaseStock(2L, 1);

    verify(orderItemMapper)
        .insertBatch(
            argThat(
                orderItems ->
                    List.of(1L, 2L)
                        .equals(
                            orderItems.stream()
                                .map(StudyJavaOrderItemDao::getGoodsId)
                                .toList())));
  }

  @Test
  void stopsOrderCreationWhenStockIsInsufficient() {
    StudyJavaGoodsDao goods = goods(1L, "限量商品", 100);
    when(goodsMapper.selectBatchIds(anyCollection())).thenReturn(List.of(goods));
    when(goodsMapper.decreaseStock(1L, 2)).thenReturn(0);

    StudyJavaException exception =
        assertThrows(
            StudyJavaException.class,
            () ->
                orderService.placeOrder(
                    placeOrder(List.of(new StudyJavaPlaceOrderItemDto(1L, 2)))));

    assertEquals("商品库存不足: 限量商品", exception.getMessage());
    verify(orderMapper, never()).insertOrder(any());
    verify(orderItemMapper, never()).insertBatch(anyList());
  }

  @Test
  void concurrentOrdersCannotExceedAtomicStockContract() throws Exception {
    int initialStock = 7;
    AtomicInteger remainingStock = new AtomicInteger(initialStock);
    AtomicLong sequence = new AtomicLong(1000);
    StudyJavaGoodsDao goods = goods(1L, "并发商品", 100);
    when(goodsMapper.selectBatchIds(anyCollection())).thenReturn(List.of(goods));
    when(goodsMapper.decreaseStock(1L, 1))
        .thenAnswer(
            invocation ->
                remainingStock.getAndUpdate(value -> value > 0 ? value - 1 : value) > 0 ? 1 : 0);
    when(orderNumberGenerator.nextOrderNumber())
        .thenAnswer(invocation -> Long.toString(sequence.incrementAndGet()));
    when(orderMapper.insertOrder(any()))
        .thenAnswer(
            invocation -> {
              StudyJavaOrderDao order = invocation.getArgument(0);
              order.setOrderId(sequence.incrementAndGet());
              return 1;
            });
    when(orderItemMapper.insertBatch(anyList())).thenReturn(1);

    List<Callable<Boolean>> attempts =
        IntStream.range(0, 100)
            .mapToObj(
                ignored ->
                    (Callable<Boolean>)
                        () -> {
                          try {
                            orderService.placeOrder(
                                placeOrder(List.of(new StudyJavaPlaceOrderItemDto(1L, 1))));
                            return true;
                          } catch (StudyJavaException exception) {
                            return false;
                          }
                        })
            .toList();
    ExecutorService executor = Executors.newFixedThreadPool(16);
    try {
      List<Future<Boolean>> results = executor.invokeAll(attempts);
      long successfulOrders =
          results.stream()
              .map(StudyJavaOrderServiceImplTest::getFuture)
              .filter(Boolean::booleanValue)
              .count();

      assertEquals(initialStock, successfulOrders);
      assertEquals(0, remainingStock.get());
      verify(orderMapper, times(initialStock)).insertOrder(any());
    } finally {
      executor.shutdownNow();
    }
  }

  @Test
  void preparesOrderWithExpectedStateCondition() {
    StudyJavaOrderDao paidOrder = order(10L, OrderStatus.PAID);
    StudyJavaOrderDao preparedOrder = order(10L, OrderStatus.PREPARED);
    when(orderMapper.getOrderInfo(any())).thenReturn(paidOrder, preparedOrder);
    when(orderMapper.transitionOrder(
            eq(10L),
            eq(OrderStatus.PAID.getCode()),
            eq(OrderStatus.PREPARED.getCode()),
            isNull(),
            isNull(),
            isNull()))
        .thenReturn(1);

    StudyJavaOrderVo result =
        orderService.transitionOrder(
            new StudyJavaOrderTransitionDto(10L, OrderAction.PREPARE, null));

    assertEquals(OrderStatus.PREPARED.getCode(), result.getOrderStatus());
    verify(orderMapper)
        .transitionOrder(
            eq(10L),
            eq(OrderStatus.PAID.getCode()),
            eq(OrderStatus.PREPARED.getCode()),
            isNull(),
            isNull(),
            isNull());
  }

  @Test
  void rejectsPayActionOnGenericTransitionEndpoint() {
    StudyJavaException exception =
        assertThrows(
            StudyJavaException.class,
            () ->
                orderService.transitionOrder(
                    new StudyJavaOrderTransitionDto(
                        10L, OrderAction.PAY, PaymentType.ALIPAY.getCode())));

    assertEquals("支付订单请使用 /order/pay 接口", exception.getMessage());
    verifyNoInteractions(orderMapper);
  }

  @Test
  void rejectsDirectPaymentStateMutationOnLegacyUpdate() {
    StudyJavaOrderDto update = new StudyJavaOrderDto();
    update.setOrderId(10L);
    update.setPayStatus(1);

    StudyJavaException exception =
        assertThrows(StudyJavaException.class, () -> orderService.updateOrder(update));

    assertEquals("订单核心字段必须通过下单、状态流转或支付接口修改", exception.getMessage());
    verifyNoInteractions(orderMapper);
  }

  @Test
  void reportsConcurrentStateChange() {
    when(orderMapper.getOrderInfo(any()))
        .thenReturn(order(10L, OrderStatus.PENDING_PAYMENT));
    when(orderMapper.transitionOrder(
            eq(10L),
            eq(OrderStatus.PENDING_PAYMENT.getCode()),
            eq(OrderStatus.MANUALLY_CLOSED.getCode()),
            eq(null),
            eq(null),
            eq(null)))
        .thenReturn(0);

    StudyJavaException exception =
        assertThrows(
            StudyJavaException.class,
            () ->
                orderService.transitionOrder(
                    new StudyJavaOrderTransitionDto(10L, OrderAction.MANUAL_CLOSE, null)));

    assertEquals("订单状态已发生变化，请刷新后重试", exception.getMessage());
  }

  private StudyJavaOrderDao order(long orderId, OrderStatus status) {
    StudyJavaOrderDao order = new StudyJavaOrderDao();
    order.setOrderId(orderId);
    order.setOrderStatus(status.getCode());
    return order;
  }

  private StudyJavaGoodsDao goods(long goodsId, String goodsName, int sellingPrice) {
    StudyJavaGoodsDao goods = new StudyJavaGoodsDao();
    goods.setGoodsId(goodsId);
    goods.setGoodsName(goodsName);
    goods.setGoodsCoverImg("/goods/" + goodsId + ".png");
    goods.setSellingPrice(sellingPrice);
    goods.setGoodsSellStatus(1);
    return goods;
  }

  private StudyJavaPlaceOrderDto placeOrder(List<StudyJavaPlaceOrderItemDto> items) {
    return new StudyJavaPlaceOrderDto(1L, "张三", "13800138000", "测试地址", items);
  }

  private static Boolean getFuture(Future<Boolean> future) {
    try {
      return future.get();
    } catch (Exception exception) {
      throw new AssertionError(exception);
    }
  }
}
