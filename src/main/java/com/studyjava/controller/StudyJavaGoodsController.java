package com.studyjava.controller;

import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.studyjava.annotation.Log;
import com.studyjava.domain.dto.StudyJavaGoodsDto;
import com.studyjava.domain.enums.BusinessType;
import com.studyjava.domain.vo.StudyJavaGoodsVo;
import com.studyjava.service.StudyJavaGoodsService;
import com.studyjava.utils.PageResult;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/goods")
public class StudyJavaGoodsController extends BaseController {

  @Resource private StudyJavaGoodsService studyJavaGoodsService;

  /**
   * 获取商品列表
   *
   * @param pageSize 每页大小
   * @param pageNum 页码
   * @param studyJavaGoodsDto 查询条件
   * @return PageResult<StudyJavaGoodsVo>
   */
  @com.studyjava.annotation.PreAuthorize("goods:query")
  @GetMapping("/getGoodsList")
  public PageResult<StudyJavaGoodsVo> getGoodsList(
      @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
      @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
      @ModelAttribute StudyJavaGoodsDto studyJavaGoodsDto) {
    IPage<StudyJavaGoodsVo> studyJavaGoodsVoPage =
        studyJavaGoodsService.getGoodsList(startPage(pageNum, pageSize), studyJavaGoodsDto);
    return PageResult.of(studyJavaGoodsVoPage.getRecords(), studyJavaGoodsVoPage.getTotal());
  }

  /**
   * 新增商品
   *
   * @param studyJavaGoodsDto 商品信息
   * @return Boolean
   */
  @Log(title = "商品管理", businessType = BusinessType.INSERT)
  @com.studyjava.annotation.PreAuthorize("goods:add")
  @PutMapping("/insertGoods")
  public Boolean insertGoods(@Valid @RequestBody StudyJavaGoodsDto studyJavaGoodsDto) {
    return studyJavaGoodsService.insertGoods(studyJavaGoodsDto);
  }
}
