package com.studyjava.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.studyjava.domain.dao.StudyJavaGoodsDao;
import com.studyjava.domain.dto.StudyJavaGoodsDto;
import com.studyjava.domain.vo.StudyJavaGoodsVo;

/**
 * @author opera
 * @description 针对表【study_java_goods】的数据库操作Service
 * @createDate 2025-01-18 23:53:59
 */
public interface StudyJavaGoodsService {
  IPage<StudyJavaGoodsVo> getGoodsList(
      IPage<StudyJavaGoodsDao> page, StudyJavaGoodsDto studyJavaGoodsDto);

  boolean insertGoods(StudyJavaGoodsDto studyJavaGoodsDto);
}
