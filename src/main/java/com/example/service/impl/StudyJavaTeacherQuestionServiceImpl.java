package com.example.service.impl;

import com.example.entity.StudyJavaTeacherQuestion;
import com.example.repository.StudyJavaTeacherQuestionRepository;
import com.example.service.StudyJavaTeacherQuestionService;
import com.example.vo.StudyJavaTeacherQuestionVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudyJavaTeacherQuestionServiceImpl implements StudyJavaTeacherQuestionService {

    @Autowired
    private StudyJavaTeacherQuestionRepository teacherQuestionRepository;

    @Override
    @Transactional
    public StudyJavaTeacherQuestionVo createQuestion(StudyJavaTeacherQuestionVo questionVo) {
        StudyJavaTeacherQuestion question = new StudyJavaTeacherQuestion();
        BeanUtils.copyProperties(questionVo, question);
        question.setCreateTime(LocalDateTime.now());
        question.setUpdateTime(LocalDateTime.now());
        question.setIsActive(true);
        question = teacherQuestionRepository.save(question);
        BeanUtils.copyProperties(question, questionVo);
        return questionVo;
    }

    @Override
    @Transactional
    public StudyJavaTeacherQuestionVo updateQuestion(StudyJavaTeacherQuestionVo questionVo) {
        StudyJavaTeacherQuestion question = teacherQuestionRepository.findById(questionVo.getId())
                .orElseThrow(() -> new RuntimeException("题目不存在"));
        BeanUtils.copyProperties(questionVo, question);
        question.setUpdateTime(LocalDateTime.now());
        question = teacherQuestionRepository.save(question);
        BeanUtils.copyProperties(question, questionVo);
        return questionVo;
    }

    @Override
    @Transactional
    public void deleteQuestion(Long id) {
        teacherQuestionRepository.deleteById(id);
    }

    @Override
    public List<StudyJavaTeacherQuestionVo> getQuestionList(Long teacherId) {
        List<StudyJavaTeacherQuestion> questions = teacherQuestionRepository.findByTeacherId(teacherId);
        return questions.stream().map(question -> {
            StudyJavaTeacherQuestionVo vo = new StudyJavaTeacherQuestionVo();
            BeanUtils.copyProperties(question, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public StudyJavaTeacherQuestionVo getQuestionDetail(Long id) {
        StudyJavaTeacherQuestion question = teacherQuestionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("题目不存在"));
        StudyJavaTeacherQuestionVo vo = new StudyJavaTeacherQuestionVo();
        BeanUtils.copyProperties(question, vo);
        return vo;
    }
} 