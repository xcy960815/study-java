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
import org.springframework.stereotype.Service;

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
     * @param page Page<StudyJavaSysUserDto>
     * @param studyJavaOrderDto StudyJavaSysUserDto
     * @return
     */
    @Override
    public IPage<StudyJavaOrderVo> getOrderList(Page<StudyJavaSysUserDto> page, StudyJavaOrderDto studyJavaOrderDto){
//        return studyJavaOrderMapper.getOrderList(page,studyJavaSysUserDto);
        return null;
    }

}




