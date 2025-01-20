package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.domain.dao.StudyJavaGoodsCategoryDao;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.domain.dto.StudyJavaGoodsCategoryDto;
import com.example.domain.vo.StudyJavaGoodsCategoryVo;

/**
* @author opera
* @description 针对表【study_java_goods_category】的数据库操作Service
* @createDate 2025-01-18 23:53:59
*/
public interface StudyJavaGoodsCategoryService extends IService<StudyJavaGoodsCategoryDao> {
    public IPage<StudyJavaGoodsCategoryDto> getGoodsCategoryList(Page<StudyJavaGoodsCategoryVo> page , StudyJavaGoodsCategoryVo studyJavaGoodsCategoryVo);
}
