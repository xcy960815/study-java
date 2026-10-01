package com.studyjava.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.studyjava.domain.dao.StudyJavaGoodsDao;
import com.studyjava.domain.dto.StudyJavaGoodsDto;
import com.studyjava.domain.vo.StudyJavaGoodsVo;
import com.studyjava.mapper.StudyJavaGoodsMapper;
import com.studyjava.service.StudyJavaGoodsCategoryService;
import com.studyjava.service.StudyJavaGoodsService;

import jakarta.annotation.Resource;

/**
 * @author opera
 * @description 针对表【study_java_goods】的数据库操作Service实现
 * @createDate 2025-01-18 23:53:59
 */
@Service
public class StudyJavaGoodsServiceImpl implements StudyJavaGoodsService {

  @Resource private StudyJavaGoodsMapper studyJavaGoodsMapper;
  @Resource private StudyJavaGoodsCategoryService studyJavaGoodsCategoryService;

  @Override
  public IPage<StudyJavaGoodsVo> getGoodsList(
      IPage<StudyJavaGoodsDao> page, StudyJavaGoodsDto studyJavaGoodsDto) {
    StudyJavaGoodsDao query = dto2Dao(studyJavaGoodsDto);
    Long categoryId = studyJavaGoodsDto == null ? null : studyJavaGoodsDto.getGoodsCategoryId();
    if (categoryId != null && categoryId != 0) {
      query.setCategoryIds(studyJavaGoodsCategoryService.listSelfAndDescendantIds(categoryId));
    }
    IPage<StudyJavaGoodsDao> studyJavaGoodsDaoPage =
        studyJavaGoodsMapper.getGoodsList(page, query);
    List<StudyJavaGoodsVo> studyJavaGoodsVoList =
        studyJavaGoodsDaoPage.getRecords().stream().map(this::dao2Vo).toList();
    // 创建新的 IPage 对象
    IPage<StudyJavaGoodsVo> studyJavaGoodsVoPage =
        new Page<>(
            studyJavaGoodsDaoPage.getCurrent(),
            studyJavaGoodsDaoPage.getSize(),
            studyJavaGoodsDaoPage.getTotal());
    studyJavaGoodsVoPage.setRecords(studyJavaGoodsVoList);
    return studyJavaGoodsVoPage;
  }

  /**
   * 新建商品
   *
   * @param studyJavaGoodsDto StudyJavaGoodsDto
   * @return StudyJavaGoodsVo
   */
  public boolean insertGoods(StudyJavaGoodsDto studyJavaGoodsDto) {
    StudyJavaGoodsDao studyJavaGoodsDao = dto2Dao(studyJavaGoodsDto);
    return studyJavaGoodsMapper.insertGoods(studyJavaGoodsDao) > 0;
  }

  /** dto 转 dao */
  private StudyJavaGoodsDao dto2Dao(StudyJavaGoodsDto studyJavaGoodsDto) {
    StudyJavaGoodsDao studyJavaGoodsDao = new StudyJavaGoodsDao();
    BeanUtils.copyProperties(studyJavaGoodsDto, studyJavaGoodsDao);
    return studyJavaGoodsDao;
  }

  /** dao 转 vo */
  private StudyJavaGoodsVo dao2Vo(StudyJavaGoodsDao studyJavaGoodsDao) {
    StudyJavaGoodsVo studyJavaGoodsVo = new StudyJavaGoodsVo();
    BeanUtils.copyProperties(studyJavaGoodsDao, studyJavaGoodsVo);
    return studyJavaGoodsVo;
  }
}
