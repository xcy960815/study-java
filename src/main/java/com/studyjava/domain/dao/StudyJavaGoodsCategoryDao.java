package com.studyjava.domain.dao;

import java.io.Serial;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

/** 商品分类 */
@TableName(value = "study_java_goods_category")
@Data
@EqualsAndHashCode(callSuper = true)
public class StudyJavaGoodsCategoryDao extends BaseDao {
  @TableId(type = IdType.AUTO)
  private Long categoryId;

  /** 上级分类，0 表示顶级 */
  private Long parentId;

  private String categoryName;

  /** 从 1 开始的层级 */
  private Integer categoryLevel;

  private Integer orderNum;

  @Serial
  @TableField(exist = false)
  private static final long serialVersionUID = 1L;
}
