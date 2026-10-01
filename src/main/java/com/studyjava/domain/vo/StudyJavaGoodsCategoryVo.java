package com.studyjava.domain.vo;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/** 商品分类，children 为空时不展开 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StudyJavaGoodsCategoryVo extends BaseVo {
  private Long categoryId;
  private Long parentId;
  private String categoryName;
  private Integer categoryLevel;
  private Integer orderNum;
  private List<StudyJavaGoodsCategoryVo> children;
}
