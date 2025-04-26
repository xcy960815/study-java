package com.example.mapper;

import com.example.domain.vo.StudyJavaQuestionVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StudyJavaQuestionMapper {
    StudyJavaQuestionVo findById(@Param("id") String id);
    List<StudyJavaQuestionVo> findAll();
    void insert(StudyJavaQuestionVo question);
    void update(StudyJavaQuestionVo question);
    void deleteById(@Param("id") String id);
    List<StudyJavaQuestionVo> generateQuestions(@Param("level") int level);
} 