package com.studyjava.domain.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/** 商品分类请求 */
@Data
public class StudyJavaGoodsCategoryDto {
  private Long categoryId;

  /** 上级分类，0 表示顶级 */
  @NotNull(message = "上级分类不能为空")
  @Min(value = 0, message = "上级分类不合法")
  private Long parentId;

  @NotBlank(message = "分类名称不能为空")
  @Size(max = 50, message = "分类名称不能超过50个字符")
  private String categoryName;

  @Min(value = 0, message = "排序不能小于0")
  private Integer orderNum;

  private String remark;
}
