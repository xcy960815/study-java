package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.domain.dto.StudyJavaGoodsDto;
import com.example.domain.vo.StudyJavaGoodsVo;

/**
* @author opera
* @description 针对表【study_java_goods】的数据库操作Service
* @createDate 2025-01-18 23:53:59
*/
public interface StudyJavaGoodsService {
    public IPage<StudyJavaGoodsVo> getGoodsList(Page<StudyJavaGoodsVo> studyJavaGoodsVoPage , StudyJavaGoodsDto studyJavaGoodsDto);
}
