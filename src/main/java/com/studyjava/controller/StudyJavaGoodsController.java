package com.studyjava.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.studyjava.domain.dto.StudyJavaGoodsDto;
import com.studyjava.domain.vo.StudyJavaGoodsVo;
import com.studyjava.service.StudyJavaGoodsService;
import com.studyjava.utils.ResponseGenerator;
import com.studyjava.utils.ResponseListResult;
import com.studyjava.utils.ResponseResult;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/goods")
public class StudyJavaGoodsController extends BaseController {

    @Resource
    private StudyJavaGoodsService studyJavaGoodsService;

    /**
     * 获取商品分类列表
     * @param pageSize 每页大小
     * @param pageNum 页码
     * @param studyJavaGoodsDto 查询条件
     * @return 商品分类列表
     */
    @GetMapping("/getGoodsList")
    public ResponseListResult<StudyJavaGoodsVo> getGoodsList(
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @ModelAttribute StudyJavaGoodsDto studyJavaGoodsDto
    ) {
        IPage<StudyJavaGoodsVo> studyJavaGoodsVoPage =  studyJavaGoodsService.getGoodsList(startPage(pageNum,pageSize),studyJavaGoodsDto);
        return ResponseGenerator.generateListResult(studyJavaGoodsVoPage.getRecords(),studyJavaGoodsVoPage.getTotal());
    }

    /**
     * 添加商品分类
     * @param studyJavaGoodsDto 商品分类信息
     * @return 添加结果
     */
    @PutMapping("/insertGoods")
    public ResponseResult<Boolean> insertGoods(@Valid @RequestBody StudyJavaGoodsDto studyJavaGoodsDto) {
        return ResponseGenerator.generateSuccessResult(studyJavaGoodsService.insertGoods(studyJavaGoodsDto)) ;
    }
//
//    /**
//     * 更新商品分类
//     * @param categoryDto 商品分类信息
//     * @return 更新结果
//     */
//    @PutMapping("/update")
//    public ResponseResult<StudyJavaGoodsVo> updateGoodsCategory(@Valid @RequestBody StudyJavaGoodsDto categoryDto) {
//        return null;
//    }
//
//    /**
//     * 删除商品分类
//     * @param categoryId 商品分类ID
//     * @return 删除结果
//     */
//    @DeleteMapping("/delete/{categoryId}")
//    public ResponseResult<Boolean> deleteGoodsCategory(@PathVariable Long categoryId) {
//        return null;
//    }
//
//    /**
//     * 获取商品分类详情
//     * @param categoryId 商品分类ID
//     * @return 商品分类详情
//     */
//    @GetMapping("/detail/{categoryId}")
//    public ResponseResult<StudyJavaGoodsVo> getGoodsCategoryDetail(@PathVariable Long categoryId) {
//        return null;
//    }
//
//    /**
//     * 获取所有分类的树形结构
//     * @return 分类树
//     */
//    @GetMapping("/tree")
//    public ResponseResult<List<Map<String, Object>>> getCategoryTree() {
//        return null;
//    }
//
//    /**
//     * 递归构建分类树
//     * @param categories 所有分类
//     * @param parentId 父分类ID
//     * @return 分类树
//     */
//    private List<Map<String, Object>> buildCategoryTree(List<StudyJavaGoodsDao> categories, Long parentId) {
//        return categories.stream()
//                .filter(category -> Objects.equals(category.getParentId(), parentId))
//                .map(category -> {
//                    Map<String, Object> node = new HashMap<>();
//                    node.put("id", category.getCategoryId());
//                    node.put("name", category.getCategoryName());
//                    node.put("level", category.getCategoryLevel());
//
//                    List<Map<String, Object>> children = buildCategoryTree(categories, category.getCategoryId());
//                    if (!children.isEmpty()) {
//                        node.put("children", children);
//                    }
//
//                    return node;
//                })
//                .collect(Collectors.toList());
//    }
}
