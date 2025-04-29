package com.example.service.imp;

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
public class StudyJavaGoodsInfoServiceImp extends ServiceImpl<StudyJavaGoodsInfoMapper, StudyJavaGoodsInfoDao>
    implements StudyJavaGoodsInfoService {

    @Resource
    private StudyJavaGoodsInfoMapper studyJavaGoodsInfoMapper;
    @Override
    public StudyJavaGoodsInfoDto getStudyJavaGoodsInfoDetail(StudyJavaGoodsInfoVo studyJavaGoodsInfo) {
        StudyJavaGoodsInfoDao studyJavaGoodsInfoDao =  studyJavaGoodsInfoMapper.getStudyJavaGoodsInfoDetail(studyJavaGoodsInfo);
        StudyJavaGoodsInfoDto studyJavaGoodsInfoDto = new StudyJavaGoodsInfoDto();
        studyJavaGoodsInfoDto.setGoodsId(studyJavaGoodsInfoDao.getGoodsId());
        studyJavaGoodsInfoDto.setGoodsName(studyJavaGoodsInfoDao.getGoodsName());
        studyJavaGoodsInfoDto.setGoodsIntro(studyJavaGoodsInfoDao.getGoodsIntro());
        studyJavaGoodsInfoDto.setGoodsCategoryId(studyJavaGoodsInfoDao.getGoodsCategoryId());
        studyJavaGoodsInfoDto.setGoodsCoverImg(studyJavaGoodsInfoDao.getGoodsCoverImg());
        studyJavaGoodsInfoDto.setGoodsCarousel(studyJavaGoodsInfoDao.getGoodsCarousel());
        studyJavaGoodsInfoDto.setGoodsDetailContent(studyJavaGoodsInfoDao.getGoodsDetailContent());
        studyJavaGoodsInfoDto.setOriginalPrice(studyJavaGoodsInfoDao.getOriginalPrice());
        return studyJavaGoodsInfoDto;
    }
}




