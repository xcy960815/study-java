# 订单学习模块

订单模块在原有 CRUD 基础上增加了状态机、事务下单、防超卖、支付策略和领域事件，主要用于练习 Java 21 与 Spring 的业务建模方式。

## 创建订单

```http
POST /order/place
Content-Type: application/json
```

```json
{
  "userId": 1,
  "userName": "张三",
  "userPhone": "13800138000",
  "userAddress": "杭州市西湖区",
  "items": [
    { "goodsId": 10003, "quantity": 2 }
  ]
}
```

- 价格从数据库读取，不接受客户端传入的总价。
- 库存使用带 `stock_num >= quantity` 条件的单条 SQL 原子扣减。
- 多商品按商品 ID 排序扣库存，降低并发事务的死锁概率。
- 库存、订单和订单明细在同一事务中写入。

## 支付订单

首次支付和安全重试必须使用相同的 `requestId`：

```http
POST /order/pay
Content-Type: application/json
```

```json
{
  "requestId": "client-generated-unique-id",
  "orderId": 21,
  "payType": 2
}
```

支付方式：

- `1`：支付宝模拟策略
- `2`：微信支付模拟策略

`study_java_order_payment.request_id` 上的唯一索引保证幂等性。重复请求不会再次执行支付策略，而是返回第一次成功的交易号，并将响应中的 `idempotent` 标记为 `true`。

支付成功会发布 `OrderPaidEvent`。监听器使用 `@TransactionalEventListener(AFTER_COMMIT)`，只在数据库事务提交后向 `/topic/orders/{userId}` 推送 WebSocket 消息。

## 订单状态流转

非支付动作使用：

```http
POST /order/transition
```

```json
{
  "orderId": 21,
  "action": "PREPARE"
}
```

合法主流程为：

```text
PENDING_PAYMENT -> PAID -> PREPARED -> SHIPPED -> COMPLETED
```

待支付订单还可以通过 `MANUAL_CLOSE`、`TIMEOUT_CLOSE` 或 `MERCHANT_CLOSE` 关闭。支付动作只能走 `/order/pay`，不能通过通用状态接口绕过支付流水。

## 涉及的 Java 技巧

- `record`：不可变请求、支付结果和领域事件。
- `sealed interface` 与模式匹配 `switch`：订单事件和状态机。
- 策略模式：`PaymentStrategy` 及其支付宝、微信实现。
- 不可变集合：支付策略注册表。
- 精确整数运算：`Math.multiplyExact` 和 `Math.addExact` 防止金额溢出。
- 并发控制：条件更新、数据库唯一索引、行锁和固定加锁顺序。
- 事务事件：提交成功后再执行通知副作用。

## 数据库更新

已有数据库需要执行 `src/main/resources/sql/study_java.sql` 中 `study_java_order_payment` 表的建表语句。不要直接在生产数据库重新执行整个初始化文件，因为该文件包含 `DROP TABLE` 和示例数据。
