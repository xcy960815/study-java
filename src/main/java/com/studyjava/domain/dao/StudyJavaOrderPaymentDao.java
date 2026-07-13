package com.studyjava.domain.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

/** 订单支付流水。requestId 的数据库唯一约束是支付幂等性的最终保障。 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("study_java_order_payment")
public class StudyJavaOrderPaymentDao extends BaseDao {

  @TableId(type = IdType.AUTO)
  private Long paymentId;

  private String requestId;
  private Long orderId;
  private Integer paymentType;
  private String transactionNo;
  private Integer paymentStatus;
  private Integer amount;
}
