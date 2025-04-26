package com.example.service.impl;

import com.example.domain.vo.StudyJavaQuestionVo;
import com.example.domain.vo.StudyJavaWrongQuestionVo;
import com.example.domain.vo.StudyJavaProgressVo;
import com.example.service.StudyJavaMathService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class StudyJavaMathServiceImpl implements StudyJavaMathService {
    private final Random random = new Random();
    private final List<StudyJavaWrongQuestionVo> wrongQuestions = new ArrayList<>();
    private StudyJavaProgressVo progress = new StudyJavaProgressVo();

    @Override
    public List<StudyJavaQuestionVo> getQuestions(int level) {
        List<StudyJavaQuestionVo> questions = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            StudyJavaQuestionVo question = new StudyJavaQuestionVo();
            question.setId("q" + i);
            question.setLevel(level);
            question.setType("addition");
            question.setContent(generateQuestion(level));
            question.setAnswer(generateAnswer(question.getContent()));
            questions.add(question);
        }
        return questions;
    }

    @Override
    public boolean submitAnswer(String questionId, int answer) {
        // 这里应该从数据库或缓存中获取正确答案
        // 为了示例，我们假设所有答案都是正确的
        boolean isCorrect = true;
        
        if (!isCorrect) {
            StudyJavaWrongQuestionVo wrongQuestion = new StudyJavaWrongQuestionVo();
            wrongQuestion.setId(questionId);
            wrongQuestion.setContent("2 + 3 = ?");
            wrongQuestion.setCorrectAnswer(5);
            wrongQuestion.setUserAnswer(answer);
            wrongQuestions.add(wrongQuestion);
        }
        
        return isCorrect;
    }

    @Override
    public List<StudyJavaWrongQuestionVo> getWrongQuestions() {
        return wrongQuestions;
    }

    @Override
    public void updateProgress(StudyJavaProgressVo progress) {
        this.progress = progress;
    }

    private String generateQuestion(int level) {
        int a = random.nextInt(10 * level);
        int b = random.nextInt(10 * level);
        return a + " + " + b + " = ?";
    }

    private int generateAnswer(String question) {
        try {
            // 清理字符串，移除所有空格
            String cleanedQuestion = question.replaceAll("\\s+", "");
            
            // 分割字符串
            String[] parts = cleanedQuestion.split("\\+");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid question format: " + question);
            }
            
            // 解析数字
            int a = Integer.parseInt(parts[0]);
            String rightPart = parts[1].split("=")[0];
            int b = Integer.parseInt(rightPart);
            
            return a + b;
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to generate answer for question: " + question, e);
        }
    }
} 