package com.studyjava.domain.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * @TableName study_java_goods
 */
@TableName(value = "study_java_goods")
@Data
public class StudyJavaGoodsDto implements Serializable {
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
  private Integer createUser;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date createTime;

  private Integer updateUser;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date updateTime;

  @Serial
  @TableField(exist = false)
  private static final long serialVersionUID = 1L;
}
