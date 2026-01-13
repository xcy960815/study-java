package com.studyjava.domain.vo;

import java.io.Serial;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @TableName study_java_goods
 */
@TableName(value = "study_java_goods")
@Data
@EqualsAndHashCode(callSuper = true)
public class StudyJavaGoodsVo extends BaseVo {
  @TableId(type = IdType.AUTO)
  private Long goodsId;

  private String goodsName;
  private String goodsIntro;
  private Long goodsCategoryId;
  private String goodsCoverImg;
  private String goodsCarousel;
  private String goodsDetailContent;
  private Integer originalPrice;
  private Integer sellingPrice;
  private Integer stockNum;
  private String tag;
  private Integer goodsSellStatus;

  @Serial
  @TableField(exist = false)
  private static final long serialVersionUID = 1L;
}
