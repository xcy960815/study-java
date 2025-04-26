package com.example.service;

import com.example.domain.vo.StudyJavaQuestionVo;
import com.example.domain.vo.StudyJavaWrongQuestionVo;
import com.example.domain.vo.StudyJavaProgressVo;
import java.util.List;

public interface StudyJavaMathService {
    List<StudyJavaQuestionVo> getQuestions(int level);
    boolean submitAnswer(String questionId, int answer);
    List<StudyJavaWrongQuestionVo> getWrongQuestions();
    void updateProgress(StudyJavaProgressVo progress);
} 