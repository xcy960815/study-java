package com.example.service;



import com.baomidou.mybatisplus.extension.service.IService;
import com.example.domain.dao.StudyJavaGoodsInfoDao;
import com.example.domain.dto.StudyJavaGoodsInfoDto;
import com.example.domain.vo.StudyJavaGoodsInfoVo;

/**
* @author opera
* @description 针对表【study_java_goods_info】的数据库操作Service
* @createDate 2025-01-24 10:59:26
*/
public interface StudyJavaGoodsInfoService extends IService<StudyJavaGoodsInfoDao> {
    public StudyJavaGoodsInfoVo getStudyJavaGoodsInfoDetail(StudyJavaGoodsInfoDto studyJavaGoodsInfoDto);
}
