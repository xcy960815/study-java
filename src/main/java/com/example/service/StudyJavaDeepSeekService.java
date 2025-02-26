package com.example.service;


import com.example.domain.vo.deeseek.StudyJavaDeepSeekCompletionsVo;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface StudyJavaDeepSeekService {
        void completions(StudyJavaDeepSeekCompletionsVo studyJavaDeepSeekCompletionsVo, SseEmitter emitter);
}
