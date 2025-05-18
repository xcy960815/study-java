package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.domain.dao.StudyJavaGoodsInfoDao;
import com.example.domain.dto.StudyJavaGoodsInfoDto;
import com.example.domain.vo.StudyJavaGoodsInfoVo;
import com.example.service.StudyJavaGoodsInfoService;
import com.example.mapper.StudyJavaGoodsInfoMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
* @author opera
* @description 针对表【study_java_goods_info】的数据库操作Service实现
* @createDate 2025-01-18 23:50:34
*/
@Service
public class StudyJavaGoodsInfoServiceImpl extends ServiceImpl<StudyJavaGoodsInfoMapper, StudyJavaGoodsInfoDao>
    implements StudyJavaGoodsInfoService {

    @Resource
    private StudyJavaGoodsInfoMapper studyJavaGoodsInfoMapper;
    @Override
    public StudyJavaGoodsInfoVo getStudyJavaGoodsInfoDetail(StudyJavaGoodsInfoDto studyJavaGoodsInfoDto) {
        StudyJavaGoodsInfoDao studyJavaGoodsInfoDao = studyJavaGoodsInfoMapper.getStudyJavaGoodsInfoDetail(studyJavaGoodsInfoDto);
        StudyJavaGoodsInfoVo studyJavaGoodsInfoVo = new StudyJavaGoodsInfoVo();
        studyJavaGoodsInfoVo.setGoodsId(studyJavaGoodsInfoDao.getGoodsId());
        studyJavaGoodsInfoVo.setGoodsName(studyJavaGoodsInfoDao.getGoodsName());
        studyJavaGoodsInfoVo.setGoodsIntro(studyJavaGoodsInfoDao.getGoodsIntro());
        studyJavaGoodsInfoVo.setGoodsCategoryId(studyJavaGoodsInfoDao.getGoodsCategoryId());
        studyJavaGoodsInfoVo.setGoodsCoverImg(studyJavaGoodsInfoDao.getGoodsCoverImg());
        studyJavaGoodsInfoVo.setGoodsCarousel(studyJavaGoodsInfoDao.getGoodsCarousel());
        studyJavaGoodsInfoVo.setGoodsDetailContent(studyJavaGoodsInfoDao.getGoodsDetailContent());
        studyJavaGoodsInfoVo.setOriginalPrice(studyJavaGoodsInfoDao.getOriginalPrice());
        studyJavaGoodsInfoVo.setSellingPrice(studyJavaGoodsInfoDao.getSellingPrice());
        studyJavaGoodsInfoVo.setStockNum(studyJavaGoodsInfoDao.getStockNum());
        studyJavaGoodsInfoVo.setTag(studyJavaGoodsInfoDao.getTag());
        studyJavaGoodsInfoVo.setGoodsSellStatus(studyJavaGoodsInfoDao.getGoodsSellStatus());
        studyJavaGoodsInfoVo.setCreateTime(studyJavaGoodsInfoDao.getCreateTime());
        studyJavaGoodsInfoVo.setUpdateTime(studyJavaGoodsInfoDao.getUpdateTime());
        return studyJavaGoodsInfoVo;
    }
}




