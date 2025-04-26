package com.example.service;

import com.example.vo.StudyJavaTeacherQuestionVo;
import java.util.List;

public interface StudyJavaTeacherQuestionService {
    /**
     * 创建题目
     */
    StudyJavaTeacherQuestionVo createQuestion(StudyJavaTeacherQuestionVo questionVo);

    /**
     * 更新题目
     */
    StudyJavaTeacherQuestionVo updateQuestion(StudyJavaTeacherQuestionVo questionVo);

    /**
     * 删除题目
     */
    void deleteQuestion(Long id);

    /**
     * 获取题目列表
     */
    List<StudyJavaTeacherQuestionVo> getQuestionList(Long teacherId);

    /**
     * 获取题目详情
     */
    StudyJavaTeacherQuestionVo getQuestionDetail(Long id);
} 