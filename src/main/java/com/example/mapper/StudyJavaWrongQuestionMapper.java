package com.example.mapper;

import com.example.domain.vo.StudyJavaWrongQuestionVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StudyJavaWrongQuestionMapper {
    StudyJavaWrongQuestionVo findById(@Param("id") String id);
    List<StudyJavaWrongQuestionVo> findAll();
    void insert(StudyJavaWrongQuestionVo wrongQuestion);
    void update(StudyJavaWrongQuestionVo wrongQuestion);
    void deleteById(@Param("id") String id);
} 