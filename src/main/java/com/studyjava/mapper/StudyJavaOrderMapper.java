package com.studyjava.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.studyjava.domain.dao.StudyJavaOrderDao;
import org.apache.ibatis.annotations.Param;

/**
* @author opera
* @description 针对表【study_java_order】的数据库操作Mapper
* @createDate 2025-01-18 23:36:52
* @Entity com.example.domain.dao.StudyJavaOrderDao
*/
public interface StudyJavaOrderMapper  {
    IPage<StudyJavaOrderDao> getOrderList(@Param("page")  IPage<StudyJavaOrderDao> page, @Param("studyJavaOrderDao") StudyJavaOrderDao studyJavaOrderDao);

    StudyJavaOrderDao getOrderInfo(@Param("studyJavaOrderDao") StudyJavaOrderDao studyJavaOrderDao);

    Boolean insertOrder(@Param("studyJavaOrderDao") StudyJavaOrderDao studyJavaOrderDao);

    Boolean updateOrder(@Param("studyJavaOrderDao") StudyJavaOrderDao studyJavaOrderDao);

    Boolean deleteOrder(@Param("studyJavaOrderDao") StudyJavaOrderDao studyJavaOrderDao);
}




