package com.example.service;

import com.example.domain.dto.ollama.*;
import com.example.domain.vo.ollama.StudyJavaOllamaChatVo;
import com.example.domain.vo.ollama.StudyJavaOllamaDeleteVo;
import com.example.domain.vo.ollama.StudyJavaOllamaGrenerateVo;
import com.example.domain.vo.ollama.StudyJavaOllamaShowVo;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;

public interface StudyJavaOllamaService {
        StudyJavaOllamaGenerateDto generate(StudyJavaOllamaGrenerateVo studyJavaOllamaGrenerateVo) throws IOException, InterruptedException;
        void generateStream(StudyJavaOllamaGrenerateVo studyJavaOllamaGrenerateVo, SseEmitter emitter);
        void chat(StudyJavaOllamaChatVo studyJavaOllamaChatVo, SseEmitter emitter);
        StudyJavaOllamaModelsDto models() throws IOException, InterruptedException;
        StudyJavaOllamaVersionDto version() throws IOException, InterruptedException;
        StudyJavaOllamaTagsDto tags() throws IOException, InterruptedException;
        Boolean delete(StudyJavaOllamaDeleteVo studyJavaOllamaDeleteVo) throws IOException, InterruptedException;
        StudyJavaOllamaPsDto ps() throws IOException, InterruptedException;
        StudyJavaOllamaShowDto show(StudyJavaOllamaShowVo studyJavaOllamaShowVo) throws IOException, InterruptedException;
//        void pull();
}
