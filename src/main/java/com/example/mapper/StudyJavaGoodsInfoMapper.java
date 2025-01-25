package com.example.mapper;

import com.example.domain.dao.StudyJavaGoodsInfoDao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.domain.vo.StudyJavaGoodsInfoVo;
import org.apache.ibatis.annotations.Param;

/**
* @author opera
* @description 针对表【study_java_goods_info】的数据库操作Mapper
* @createDate 2025-01-24 10:59:26
* @Entity com.example.domain.dao.StudyJavaGoodsInfo
*/
public interface StudyJavaGoodsInfoMapper extends BaseMapper<StudyJavaGoodsInfoDao> {

    public StudyJavaGoodsInfoDao getStudyJavaGoodsInfoDetail(@Param("studyJavaGoodsInfo") StudyJavaGoodsInfoVo studyJavaGoodsInfo);
}




