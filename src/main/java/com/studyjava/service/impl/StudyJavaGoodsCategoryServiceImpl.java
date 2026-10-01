package com.studyjava.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.studyjava.domain.dao.StudyJavaGoodsCategoryDao;
import com.studyjava.domain.dao.StudyJavaGoodsDao;
import com.studyjava.domain.dto.StudyJavaGoodsCategoryDto;
import com.studyjava.domain.vo.StudyJavaGoodsCategoryVo;
import com.studyjava.exception.StudyJavaException;
import com.studyjava.mapper.StudyJavaGoodsCategoryMapper;
import com.studyjava.mapper.StudyJavaGoodsMapper;
import com.studyjava.service.StudyJavaGoodsCategoryService;

import jakarta.annotation.Resource;

/** 商品分类 */
@Service
public class StudyJavaGoodsCategoryServiceImpl implements StudyJavaGoodsCategoryService {
  private static final long ROOT_PARENT_ID = 0L;
  private static final int MAX_LEVEL = 20;

  @Resource private StudyJavaGoodsCategoryMapper categoryMapper;
  @Resource private StudyJavaGoodsMapper goodsMapper;

  @Override
  public List<StudyJavaGoodsCategoryVo> getCategoryTree() {
    List<StudyJavaGoodsCategoryDao> categories = categoryMapper.selectList(null);
    return buildTree(categories, ROOT_PARENT_ID);
  }

  @Override
  public List<Long> listSelfAndDescendantIds(Long categoryId) {
    List<Long> ids = new ArrayList<>();
    if (categoryId == null || categoryId == ROOT_PARENT_ID) {
      return ids;
    }
    ids.add(categoryId);
    appendDescendantIds(categoryMapper.selectList(null), categoryId, ids, 0);
    return ids;
  }

  @Override
  public boolean insertCategory(StudyJavaGoodsCategoryDto categoryDto) {
    StudyJavaGoodsCategoryDao category = new StudyJavaGoodsCategoryDao();
    category.setParentId(normalizeParentId(categoryDto.getParentId()));
    category.setCategoryName(categoryDto.getCategoryName().trim());
    category.setOrderNum(categoryDto.getOrderNum() == null ? 0 : categoryDto.getOrderNum());
    category.setRemark(categoryDto.getRemark());
    category.setCategoryLevel(resolveLevel(null, category.getParentId()));
    return categoryMapper.insert(category) > 0;
  }

  @Override
  public boolean updateCategory(StudyJavaGoodsCategoryDto categoryDto) {
    if (categoryDto.getCategoryId() == null) {
      throw new StudyJavaException("分类不存在");
    }
    StudyJavaGoodsCategoryDao current = categoryMapper.selectById(categoryDto.getCategoryId());
    if (current == null) {
      throw new StudyJavaException("分类不存在");
    }
    Long parentId = normalizeParentId(categoryDto.getParentId());
    current.setParentId(parentId);
    current.setCategoryName(categoryDto.getCategoryName().trim());
    current.setOrderNum(categoryDto.getOrderNum() == null ? 0 : categoryDto.getOrderNum());
    current.setRemark(categoryDto.getRemark());
    current.setCategoryLevel(resolveLevel(current.getCategoryId(), parentId));
    boolean updated = categoryMapper.updateById(current) > 0;
    refreshDescendantLevels(current.getCategoryId(), current.getCategoryLevel(), 0);
    return updated;
  }

  @Override
  public boolean deleteCategory(Long categoryId) {
    StudyJavaGoodsCategoryDao current = categoryMapper.selectById(categoryId);
    if (current == null) {
      throw new StudyJavaException("分类不存在");
    }
    Long childCount =
        categoryMapper.selectCount(
            new LambdaQueryWrapper<StudyJavaGoodsCategoryDao>()
                .eq(StudyJavaGoodsCategoryDao::getParentId, categoryId));
    if (childCount != null && childCount > 0) {
      throw new StudyJavaException("请先删除下级分类");
    }
    Long goodsCount =
        goodsMapper.selectCount(
            new LambdaQueryWrapper<StudyJavaGoodsDao>()
                .eq(StudyJavaGoodsDao::getGoodsCategoryId, categoryId));
    if (goodsCount != null && goodsCount > 0) {
      throw new StudyJavaException("分类下还有商品，不能删除");
    }
    return categoryMapper.deleteById(categoryId) > 0;
  }

  private List<StudyJavaGoodsCategoryVo> buildTree(
      List<StudyJavaGoodsCategoryDao> categories, long parentId) {
    return categories.stream()
        .filter(category -> parentId == normalizeParentId(category.getParentId()))
        .sorted(
            Comparator.comparing(
                StudyJavaGoodsCategoryDao::getOrderNum, Comparator.nullsLast(Integer::compareTo)))
        .map(
            category -> {
              StudyJavaGoodsCategoryVo categoryVo = toVo(category);
              List<StudyJavaGoodsCategoryVo> children =
                  buildTree(categories, category.getCategoryId());
              categoryVo.setChildren(children.isEmpty() ? null : children);
              return categoryVo;
            })
        .toList();
  }

  private void appendDescendantIds(
      List<StudyJavaGoodsCategoryDao> categories, Long parentId, List<Long> ids, int depth) {
    if (depth >= MAX_LEVEL) {
      return;
    }
    for (StudyJavaGoodsCategoryDao category : categories) {
      if (parentId.equals(normalizeParentId(category.getParentId()))) {
        ids.add(category.getCategoryId());
        appendDescendantIds(categories, category.getCategoryId(), ids, depth + 1);
      }
    }
  }

  private int resolveLevel(Long categoryId, Long parentId) {
    if (parentId == ROOT_PARENT_ID) {
      return 1;
    }
    assertParentIsNotDescendant(categoryId, parentId);
    StudyJavaGoodsCategoryDao parent = categoryMapper.selectById(parentId);
    if (parent == null) {
      throw new StudyJavaException("上级分类不存在");
    }
    int parentLevel = parent.getCategoryLevel() == null ? 1 : parent.getCategoryLevel();
    if (parentLevel >= MAX_LEVEL) {
      throw new StudyJavaException("分类层级过深");
    }
    return parentLevel + 1;
  }

  /** 沿着上级一直往上走，碰到自己说明形成了环。 */
  private void assertParentIsNotDescendant(Long categoryId, Long parentId) {
    if (categoryId == null) {
      return;
    }
    if (categoryId.equals(parentId)) {
      throw new StudyJavaException("上级分类不能是自己");
    }
    int depth = 0;
    Long currentId = parentId;
    while (currentId != null && currentId != ROOT_PARENT_ID) {
      if (depth++ >= MAX_LEVEL) {
        throw new StudyJavaException("分类层级过深");
      }
      if (categoryId.equals(currentId)) {
        throw new StudyJavaException("上级分类不能是自己的子分类");
      }
      StudyJavaGoodsCategoryDao parent = categoryMapper.selectById(currentId);
      if (parent == null) {
        throw new StudyJavaException("上级分类不存在");
      }
      currentId = parent.getParentId();
    }
  }

  private void refreshDescendantLevels(Long categoryId, int level, int depth) {
    if (depth >= MAX_LEVEL) {
      return;
    }
    List<StudyJavaGoodsCategoryDao> children =
        categoryMapper.selectList(
            new LambdaQueryWrapper<StudyJavaGoodsCategoryDao>()
                .eq(StudyJavaGoodsCategoryDao::getParentId, categoryId));
    for (StudyJavaGoodsCategoryDao child : children) {
      child.setCategoryLevel(level + 1);
      categoryMapper.updateById(child);
      refreshDescendantLevels(child.getCategoryId(), level + 1, depth + 1);
    }
  }

  private StudyJavaGoodsCategoryVo toVo(StudyJavaGoodsCategoryDao category) {
    StudyJavaGoodsCategoryVo categoryVo = new StudyJavaGoodsCategoryVo();
    BeanUtils.copyProperties(category, categoryVo);
    return categoryVo;
  }

  private long normalizeParentId(Long parentId) {
    return parentId == null ? ROOT_PARENT_ID : parentId;
  }
}
