package com.example.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.domain.dao.StudyJavaOrderDao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.domain.dao.StudyJavaSysMenuDao;
import org.apache.ibatis.annotations.Param;

/**
* @author opera
* @description 针对表【study_java_order】的数据库操作Mapper
* @createDate 2025-01-18 23:36:52
* @Entity com.example.domain.dao.StudyJavaOrderDao
*/
public interface StudyJavaOrderMapper  {
    IPage<StudyJavaOrderDao> getOrderList(Page<StudyJavaOrderDao> page, @Param("studyJavaOrderDao") StudyJavaOrderDao studyJavaOrderDao);
}




