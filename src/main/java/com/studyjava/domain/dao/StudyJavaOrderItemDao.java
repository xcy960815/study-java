package com.studyjava.domain.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

/** 订单商品快照。商品改名或改价后，历史订单仍保留创建时的数据。 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("study_java_order_item")
public class StudyJavaOrderItemDao extends BaseDao {

  @TableId(type = IdType.AUTO)
  private Long orderItemId;

  private Long orderId;
  private Long goodsId;
  private String goodsName;
  private String goodsCoverImg;
  private Integer sellingPrice;
  private Integer goodsCount;
}
