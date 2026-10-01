package com.studyjava.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.studyjava.annotation.Log;
import com.studyjava.domain.dto.StudyJavaGoodsCategoryDto;
import com.studyjava.domain.enums.BusinessType;
import com.studyjava.domain.vo.StudyJavaGoodsCategoryVo;
import com.studyjava.service.StudyJavaGoodsCategoryService;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

/** 商品分类 */
@RestController
@RequestMapping("/goods/category")
public class StudyJavaGoodsCategoryController {

  @Resource private StudyJavaGoodsCategoryService studyJavaGoodsCategoryService;

  @com.studyjava.annotation.PreAuthorize("goods:query")
  @GetMapping("/tree")
  public List<StudyJavaGoodsCategoryVo> getCategoryTree() {
    return studyJavaGoodsCategoryService.getCategoryTree();
  }

  @Log(title = "商品分类", businessType = BusinessType.INSERT)
  @com.studyjava.annotation.PreAuthorize("goods:add")
  @PostMapping
  public Boolean insertCategory(@Valid @RequestBody StudyJavaGoodsCategoryDto categoryDto) {
    return studyJavaGoodsCategoryService.insertCategory(categoryDto);
  }

  @Log(title = "商品分类", businessType = BusinessType.UPDATE)
  @com.studyjava.annotation.PreAuthorize("goods:edit")
  @PutMapping
  public Boolean updateCategory(@Valid @RequestBody StudyJavaGoodsCategoryDto categoryDto) {
    return studyJavaGoodsCategoryService.updateCategory(categoryDto);
  }

  @Log(title = "商品分类", businessType = BusinessType.DELETE)
  @com.studyjava.annotation.PreAuthorize("goods:remove")
  @DeleteMapping("/{categoryId}")
  public Boolean deleteCategory(@PathVariable Long categoryId) {
    return studyJavaGoodsCategoryService.deleteCategory(categoryId);
  }
}
