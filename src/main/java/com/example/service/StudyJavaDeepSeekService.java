package com.example.service;


import com.example.domain.dto.deepseek.StudyJavaDeepSeekModelsDto;
import com.example.domain.vo.deeseek.StudyJavaDeepSeekCompletionsVo;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

public interface StudyJavaDeepSeekService {
        void completions(StudyJavaDeepSeekCompletionsVo studyJavaDeepSeekCompletionsVo, SseEmitter emitter);

        StudyJavaDeepSeekModelsDto models() throws IOException, InterruptedException;;
}
