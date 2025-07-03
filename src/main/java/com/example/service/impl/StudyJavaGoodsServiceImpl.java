package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.domain.dao.StudyJavaGoodsDao;
import com.example.domain.dto.StudyJavaGoodsDto;
import com.example.domain.vo.StudyJavaGoodsVo;
import com.example.service.StudyJavaGoodsService;
import com.example.mapper.StudyJavaGoodsMapper;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;


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
    public IPage<StudyJavaGoodsVo> getGoodsList (IPage<StudyJavaGoodsDao> page , StudyJavaGoodsDto studyJavaGoodsDto){
        IPage<StudyJavaGoodsDao> studyJavaGoodsDaoPage = studyJavaGoodsMapper.getGoodsList(page, dto2Dao(studyJavaGoodsDto));
        List<StudyJavaGoodsVo> studyJavaGoodsVoList = studyJavaGoodsDaoPage.getRecords().stream().map(this::dao2Vo).toList();
        // 创建新的 IPage 对象
        IPage<StudyJavaGoodsVo> studyJavaGoodsVoPage = new Page<>(studyJavaGoodsDaoPage.getCurrent(), studyJavaGoodsDaoPage.getSize(), studyJavaGoodsDaoPage.getTotal());
        studyJavaGoodsVoPage.setRecords(studyJavaGoodsVoList);
        return studyJavaGoodsVoPage;
    }

    /**
     * 新建商品
     * @param studyJavaGoodsDto StudyJavaGoodsDto
     * @return StudyJavaGoodsVo
     */
    public boolean insertGoods(StudyJavaGoodsDto studyJavaGoodsDto){
        StudyJavaGoodsDao studyJavaGoodsDao = dto2Dao(studyJavaGoodsDto);
        return studyJavaGoodsMapper.insertGoods(studyJavaGoodsDao) > 0;
    }

    /**
     * dto 转 dao
     */
    private StudyJavaGoodsDao dto2Dao(StudyJavaGoodsDto studyJavaGoodsDto){
        StudyJavaGoodsDao studyJavaGoodsDao = new StudyJavaGoodsDao();
        BeanUtils.copyProperties(studyJavaGoodsDto, studyJavaGoodsDao);
        return studyJavaGoodsDao;
    }

    /**
     * dao 转 vo
     */
    private StudyJavaGoodsVo dao2Vo(StudyJavaGoodsDao studyJavaGoodsDao){
        StudyJavaGoodsVo studyJavaGoodsVo = new StudyJavaGoodsVo();
        BeanUtils.copyProperties(studyJavaGoodsDao, studyJavaGoodsVo);
        return studyJavaGoodsVo;
    }


}




