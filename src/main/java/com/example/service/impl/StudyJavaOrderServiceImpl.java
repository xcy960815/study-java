package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.domain.dao.StudyJavaOrderDao;
import com.example.domain.dto.StudyJavaOrderDto;
import com.example.domain.dto.StudyJavaSysUserDto;
import com.example.domain.vo.StudyJavaOrderVo;
import com.example.service.StudyJavaOrderService;
import com.example.mapper.StudyJavaOrderMapper;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author opera
* @description 针对表【study_java_order】的数据库操作Service实现
* @createDate 2025-01-18 23:36:52
*/
@Service
public class StudyJavaOrderServiceImpl implements StudyJavaOrderService {
    @Resource
    private StudyJavaOrderMapper studyJavaOrderMapper;


    /**
     * 获取订单列表
     * @param page Page<StudyJavaOrderDao>
     * @param studyJavaOrderDto StudyJavaOrderDto
     * @return
     */
    @Override
    public IPage<StudyJavaOrderVo> getOrderList(IPage<com.example.domain.dao.StudyJavaOrderDao> page, StudyJavaOrderDto studyJavaOrderDto) {
        StudyJavaOrderDao studyJavaOrderDao = convertToDao(studyJavaOrderDto);
        IPage<StudyJavaOrderDao> studyJavaOrderResponseDao = studyJavaOrderMapper.getOrderList(page, studyJavaOrderDao);
        // 转换为 VO 并组装新分页对象
        List<StudyJavaOrderVo> voList = studyJavaOrderResponseDao.getRecords().stream()
            .map(this::convertToVo)
            .collect(Collectors.toList());
        IPage<StudyJavaOrderVo> voPage = new Page<>(studyJavaOrderResponseDao.getCurrent(), studyJavaOrderResponseDao.getSize(), studyJavaOrderResponseDao.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }
    /**
     * dao 转 vo
     */
    private StudyJavaOrderVo convertToVo(StudyJavaOrderDao studyJavaOrderDao) {
        if (studyJavaOrderDao == null) {
            return null;
        }
        StudyJavaOrderVo studyJavaOrderVo = new StudyJavaOrderVo();
        BeanUtils.copyProperties(studyJavaOrderDao, studyJavaOrderVo);
        return studyJavaOrderVo;
    }

    /**
     * vo 转 dao
     */
    // private StudyJavaOrderDao convertToDao(StudyJavaOrderVo studyJavaOrderVo) {
    //     if (studyJavaOrderVo == null) {
    //         return null;
    //     }
    //     StudyJavaOrderDao studyJavaOrderDao = new StudyJavaOrderDao();
    //     BeanUtils.copyProperties(studyJavaOrderVo, studyJavaOrderDao);
    //     return studyJavaOrderDao;
    // }

    /**
     * dto 转 dao
     */
    private StudyJavaOrderDao convertToDao(StudyJavaOrderDto studyJavaOrderDto) {
        if (studyJavaOrderDto == null) {
            return null;
        }
        StudyJavaOrderDao studyJavaOrderDao = new StudyJavaOrderDao();
        BeanUtils.copyProperties(studyJavaOrderDto, studyJavaOrderDao);
        return studyJavaOrderDao;
    }
}


