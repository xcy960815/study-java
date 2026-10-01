package com.studyjava.service;

import java.util.List;

import com.studyjava.domain.dto.StudyJavaGoodsCategoryDto;
import com.studyjava.domain.vo.StudyJavaGoodsCategoryVo;

/** 商品分类 */
public interface StudyJavaGoodsCategoryService {
  List<StudyJavaGoodsCategoryVo> getCategoryTree();

  /** 包含自身和全部下级，供商品列表按父分类筛选。 */
  List<Long> listSelfAndDescendantIds(Long categoryId);

  boolean insertCategory(StudyJavaGoodsCategoryDto categoryDto);

  boolean updateCategory(StudyJavaGoodsCategoryDto categoryDto);

  boolean deleteCategory(Long categoryId);
}
