package com.studyjava.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.studyjava.domain.dao.StudyJavaOrderItemDao;

public interface StudyJavaOrderItemMapper {

  int insertBatch(@Param("items") List<StudyJavaOrderItemDao> items);
}
