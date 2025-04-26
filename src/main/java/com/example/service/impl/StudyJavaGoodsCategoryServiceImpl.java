package com.example.service.imp;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.domain.dao.StudyJavaGoodsCategoryDao;
import com.example.domain.dto.StudyJavaGoodsCategoryDto;
import com.example.domain.vo.StudyJavaGoodsCategoryVo;
import com.example.service.StudyJavaGoodsCategoryService;
import com.example.mapper.StudyJavaGoodsCategoryMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author opera
* @description 针对表【study_java_goods_category】的数据库操作Service实现
* @createDate 2025-01-18 23:53:59
*/
@Service
public class StudyJavaGoodsCategoryServiceImpl extends ServiceImpl<StudyJavaGoodsCategoryMapper, StudyJavaGoodsCategoryDao>
    implements StudyJavaGoodsCategoryService {

    @Resource
    private  StudyJavaGoodsCategoryMapper studyJavaGoodsCategoryMapper;

    @Override
    public IPage<StudyJavaGoodsCategoryDto> getGoodsCategoryList(Page<StudyJavaGoodsCategoryVo> page , StudyJavaGoodsCategoryVo studyJavaGoodsCategoryVo){

        IPage<StudyJavaGoodsCategoryDao> studyJavaGoodsCategoryIpage = studyJavaGoodsCategoryMapper.getGoodsCategoryList(page,studyJavaGoodsCategoryVo);

        List<StudyJavaGoodsCategoryDto> goodsCategoryList = studyJavaGoodsCategoryIpage.getRecords().stream().map(goodsCategory -> {
            StudyJavaGoodsCategoryDto studyJavaGoodsCategoryDto = new StudyJavaGoodsCategoryDto();
            studyJavaGoodsCategoryDto.setCategoryId(goodsCategory.getCategoryId());
            studyJavaGoodsCategoryDto.setCategoryName(goodsCategory.getCategoryName());
            Integer categoryLevel = goodsCategory.getCategoryLevel();
            String categoryLevelStr = "";
            if (categoryLevel == 1) {
                categoryLevelStr = "一级分类";
            }else if(categoryLevel == 2){
                categoryLevelStr = "二级分类";
            }else {
                categoryLevelStr = "三级分类";
            }
            studyJavaGoodsCategoryDto.setCategoryLevel(categoryLevelStr);
            studyJavaGoodsCategoryDto.setCreateTime(goodsCategory.getCreateTime());
            studyJavaGoodsCategoryDto.setUpdateTime(goodsCategory.getUpdateTime());
            studyJavaGoodsCategoryDto.setCreateUser(goodsCategory.getCreateUser());
            studyJavaGoodsCategoryDto.setUpdateUser(goodsCategory.getUpdateUser());
            studyJavaGoodsCategoryDto.setParentId(goodsCategory.getParentId());
            return studyJavaGoodsCategoryDto;
        }).toList();

        // 创建新的 IPage 对象
        IPage<StudyJavaGoodsCategoryDto> resultPage = new Page<>(studyJavaGoodsCategoryIpage.getCurrent(), studyJavaGoodsCategoryIpage.getSize(), studyJavaGoodsCategoryIpage.getTotal());
        resultPage.setRecords(goodsCategoryList);
        return resultPage;
    }
}




