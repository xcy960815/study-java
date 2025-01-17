package com.example.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.domain.dao.StudyJavaGoodsCategoryDao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.example.domain.vo.StudyJavaGoodsCategoryVo;
import org.apache.ibatis.annotations.Param;

/**
* @author opera
* @description 针对表【study_java_goods_category】的数据库操作Mapper
* @createDate 2025-01-18 23:53:59
* @Entity com.example.domain.dao.StudyJavaGoodsCategoryDao
*/
public interface StudyJavaGoodsCategoryMapper extends BaseMapper<StudyJavaGoodsCategoryDao> {


    // 获取商品列表
    IPage<StudyJavaGoodsCategoryDao> getGoodsCategoryList(@Param("page") Page<StudyJavaGoodsCategoryVo> page, @Param("studyJavaGoodsCategoryVo") StudyJavaGoodsCategoryVo studyJavaGoodsCategoryVo);
}




