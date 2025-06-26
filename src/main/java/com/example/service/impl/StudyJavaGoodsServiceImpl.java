package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.example.domain.dao.StudyJavaGoodsDao;
import com.example.domain.dto.StudyJavaGoodsDto;
import com.example.domain.vo.StudyJavaGoodsVo;
import com.example.service.StudyJavaGoodsService;
import com.example.mapper.StudyJavaGoodsMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;


/**
* @author opera
* @description 针对表【study_java_goods】的数据库操作Service实现
* @createDate 2025-01-18 23:53:59
*/
@Service
public class StudyJavaGoodsServiceImpl implements StudyJavaGoodsService {

    @Resource
    private  StudyJavaGoodsMapper studyJavaGoodsMapper;

    @Override
    public IPage<StudyJavaGoodsVo> getGoodsList (Page<StudyJavaGoodsVo> page , StudyJavaGoodsDto studyJavaGoodsDto){

//        IPage<StudyJavaGoodsDao> studyJavaGoodsCategoryIpage = studyJavaGoodsCategoryMapper.getGoodsCategoryList(page, studyJavaGoodsVo);
//
//        List<StudyJavaGoodsDto> goodsCategoryList = studyJavaGoodsCategoryIpage.getRecords().stream().map(goodsCategory -> {
//            StudyJavaGoodsDto studyJavaGoodsDto = new StudyJavaGoodsDto();
//            studyJavaGoodsDto.setCategoryId(goodsCategory.getCategoryId());
//            studyJavaGoodsDto.setCategoryName(goodsCategory.getCategoryName());
//            Integer categoryLevel = goodsCategory.getCategoryLevel();
//            String categoryLevelStr;
//            if (categoryLevel == 1) {
//                categoryLevelStr = "一级分类";
//            }else if(categoryLevel == 2){
//                categoryLevelStr = "二级分类";
//            }else {
//                categoryLevelStr = "三级分类";
//            }
//            studyJavaGoodsDto.setCategoryLevel(categoryLevelStr);
//            studyJavaGoodsDto.setCreateTime(goodsCategory.getCreateTime());
//            studyJavaGoodsDto.setUpdateTime(goodsCategory.getUpdateTime());
//            studyJavaGoodsDto.setCreateUser(goodsCategory.getCreateUser());
//            studyJavaGoodsDto.setUpdateUser(goodsCategory.getUpdateUser());
//            studyJavaGoodsDto.setParentId(goodsCategory.getParentId());
//            return studyJavaGoodsDto;
//        }).toList();
//
//        // 创建新的 IPage 对象
//        IPage<StudyJavaGoodsDto> resultPage = new Page<>(studyJavaGoodsCategoryIpage.getCurrent(), studyJavaGoodsCategoryIpage.getSize(), studyJavaGoodsCategoryIpage.getTotal());
//        resultPage.setRecords(goodsCategoryList);
//        return resultPage;
        return null;
    }
}




