package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.domain.dao.StudyJavaGoodsCategoryDao;
import com.example.domain.dto.StudyJavaGoodsCategoryDto;
import com.example.domain.vo.StudyJavaGoodsCategoryVo;
import com.example.service.StudyJavaGoodsCategoryService;
import com.example.utils.ResponseGenerator;
import com.example.utils.ResponseResult;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/goodsCategory")
public class StudyJavaGoodsCategoryController extends BaseController {

    @Resource
    private StudyJavaGoodsCategoryService studyJavaGoodsCategoryService;

    /**
     * 获取商品分类列表
     * @param pageSize 每页大小
     * @param pageNum 页码
     * @param categoryDto 查询条件
     * @return 商品分类列表
     */
    @GetMapping("/list")
    public ResponseResult<Map<String, Object>> getGoodsCategoryList(
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @ModelAttribute StudyJavaGoodsCategoryDto categoryDto
    ) {
        return null;
    }

    /**
     * 添加商品分类
     * @param categoryDto 商品分类信息
     * @return 添加结果
     */
    @PostMapping("/add")
    public ResponseResult<StudyJavaGoodsCategoryVo> addGoodsCategory(@Valid @RequestBody StudyJavaGoodsCategoryDto categoryDto) {
        return null;
    }

    /**
     * 更新商品分类
     * @param categoryDto 商品分类信息
     * @return 更新结果
     */
    @PutMapping("/update")
    public ResponseResult<StudyJavaGoodsCategoryVo> updateGoodsCategory(@Valid @RequestBody StudyJavaGoodsCategoryDto categoryDto) {
        return null;
    }

    /**
     * 删除商品分类
     * @param categoryId 商品分类ID
     * @return 删除结果
     */
    @DeleteMapping("/delete/{categoryId}")
    public ResponseResult<Boolean> deleteGoodsCategory(@PathVariable Long categoryId) {
        return null;
    }

    /**
     * 获取商品分类详情
     * @param categoryId 商品分类ID
     * @return 商品分类详情
     */
    @GetMapping("/detail/{categoryId}")
    public ResponseResult<StudyJavaGoodsCategoryVo> getGoodsCategoryDetail(@PathVariable Long categoryId) {
        return null;
    }

    /**
     * 获取所有分类的树形结构
     * @return 分类树
     */
    @GetMapping("/tree")
    public ResponseResult<List<Map<String, Object>>> getCategoryTree() {
        return null;
    }

    /**
     * 递归构建分类树
     * @param categories 所有分类
     * @param parentId 父分类ID
     * @return 分类树
     */
    private List<Map<String, Object>> buildCategoryTree(List<StudyJavaGoodsCategoryDao> categories, Long parentId) {
        return categories.stream()
                .filter(category -> Objects.equals(category.getParentId(), parentId))
                .map(category -> {
                    Map<String, Object> node = new HashMap<>();
                    node.put("id", category.getCategoryId());
                    node.put("name", category.getCategoryName());
                    node.put("level", category.getCategoryLevel());

                    List<Map<String, Object>> children = buildCategoryTree(categories, category.getCategoryId());
                    if (!children.isEmpty()) {
                        node.put("children", children);
                    }

                    return node;
                })
                .collect(Collectors.toList());
    }
}
