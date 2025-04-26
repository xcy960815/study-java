package com.example.mapper;

import com.example.domain.vo.StudyJavaProgressVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface StudyJavaProgressMapper {
    StudyJavaProgressVo findById(@Param("id") String id);
    void insert(StudyJavaProgressVo progress);
    void update(StudyJavaProgressVo progress);
    void deleteById(@Param("id") String id);
} 