package com.studyjava.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.studyjava.domain.dao.StudyJavaGoodsDao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
* @author opera
* @description 针对表【study_java_goods_category】的数据库操作Mapper
* @createDate 2025-01-18 23:53:59
* @Entity com.example.domain.dao.StudyJavaGoodsCategoryDao
*/
public interface StudyJavaGoodsMapper extends BaseMapper<StudyJavaGoodsDao> {

    // 获取商品列表
    IPage<StudyJavaGoodsDao> getGoodsList(@Param("page") IPage<StudyJavaGoodsDao> page, @Param("studyJavaGoodsDao") StudyJavaGoodsDao studyJavaGoodsDao);


    int insertGoods(StudyJavaGoodsDao studyJavaGoodsDao);
}




