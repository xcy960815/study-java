package com.studyjava.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.studyjava.domain.dao.StudyJavaGoodsCategoryDao;
import com.studyjava.domain.dto.StudyJavaGoodsCategoryDto;
import com.studyjava.domain.vo.StudyJavaGoodsCategoryVo;
import com.studyjava.exception.StudyJavaException;
import com.studyjava.mapper.StudyJavaGoodsCategoryMapper;
import com.studyjava.mapper.StudyJavaGoodsMapper;

@ExtendWith(MockitoExtension.class)
class StudyJavaGoodsCategoryServiceImplTest {

  @Mock private StudyJavaGoodsCategoryMapper categoryMapper;
  @Mock private StudyJavaGoodsMapper goodsMapper;
  @InjectMocks private StudyJavaGoodsCategoryServiceImpl service;

  @Test
  void getCategoryTreeNestsChildrenAndHidesEmptyChildren() {
    when(categoryMapper.selectList(null))
        .thenReturn(
            List.of(category(1L, 0L, "护肤", 1, 1), category(11L, 1L, "化妆水", 2, 1)));

    List<StudyJavaGoodsCategoryVo> tree = service.getCategoryTree();

    assertEquals(1, tree.size());
    assertEquals("护肤", tree.get(0).getCategoryName());
    assertEquals(1, tree.get(0).getChildren().size());
    assertEquals("化妆水", tree.get(0).getChildren().get(0).getCategoryName());
    assertNull(tree.get(0).getChildren().get(0).getChildren());
  }

  @Test
  void listSelfAndDescendantIdsIncludesChildren() {
    when(categoryMapper.selectList(null))
        .thenReturn(
            List.of(
                category(1L, 0L, "护肤", 1, 1),
                category(11L, 1L, "化妆水", 2, 1),
                category(2L, 0L, "文具", 1, 2)));

    assertEquals(List.of(1L, 11L), service.listSelfAndDescendantIds(1L));
  }

  @Test
  void insertCategoryUsesParentLevel() {
    when(categoryMapper.selectById(1L)).thenReturn(category(1L, 0L, "护肤", 1, 1));
    when(categoryMapper.insert(any(StudyJavaGoodsCategoryDao.class))).thenReturn(1);

    assertTrue(service.insertCategory(dto(null, 1L, " 化妆水 ")));
    verify(categoryMapper)
        .insert(argThat(category -> category.getCategoryLevel() == 2 && "化妆水".equals(category.getCategoryName())));
  }

  @Test
  void updateCategoryRejectsSelfAsParent() {
    when(categoryMapper.selectById(11L)).thenReturn(category(11L, 1L, "化妆水", 2, 1));

    StudyJavaException error =
        assertThrows(StudyJavaException.class, () -> service.updateCategory(dto(11L, 11L, "化妆水")));

    assertEquals("上级分类不能是自己", error.getMessage());
    verify(categoryMapper, never()).updateById(any(StudyJavaGoodsCategoryDao.class));
  }

  @Test
  void deleteCategoryRejectsWhenChildrenExist() {
    when(categoryMapper.selectById(1L)).thenReturn(category(1L, 0L, "护肤", 1, 1));
    when(categoryMapper.selectCount(any())).thenReturn(1L);

    StudyJavaException error =
        assertThrows(StudyJavaException.class, () -> service.deleteCategory(1L));

    assertEquals("请先删除下级分类", error.getMessage());
    verify(goodsMapper, never()).selectCount(any());
  }

  @Test
  void deleteCategoryRejectsWhenGoodsExist() {
    when(categoryMapper.selectById(11L)).thenReturn(category(11L, 1L, "化妆水", 2, 1));
    when(categoryMapper.selectCount(any())).thenReturn(0L);
    when(goodsMapper.selectCount(any())).thenReturn(3L);

    StudyJavaException error =
        assertThrows(StudyJavaException.class, () -> service.deleteCategory(11L));

    assertEquals("分类下还有商品，不能删除", error.getMessage());
    verify(categoryMapper, never()).deleteById(11L);
  }

  private StudyJavaGoodsCategoryDao category(
      Long categoryId, Long parentId, String categoryName, int level, int orderNum) {
    StudyJavaGoodsCategoryDao category = new StudyJavaGoodsCategoryDao();
    category.setCategoryId(categoryId);
    category.setParentId(parentId);
    category.setCategoryName(categoryName);
    category.setCategoryLevel(level);
    category.setOrderNum(orderNum);
    return category;
  }

  private StudyJavaGoodsCategoryDto dto(Long categoryId, Long parentId, String categoryName) {
    StudyJavaGoodsCategoryDto categoryDto = new StudyJavaGoodsCategoryDto();
    categoryDto.setCategoryId(categoryId);
    categoryDto.setParentId(parentId);
    categoryDto.setCategoryName(categoryName);
    categoryDto.setOrderNum(1);
    return categoryDto;
  }
}
