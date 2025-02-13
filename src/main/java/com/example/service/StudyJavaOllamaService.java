package com.example.service;

import com.example.domain.dto.ollama.*;
import com.example.domain.vo.ollama.StudyJavaOllamaDeleteVo;
import com.example.domain.vo.ollama.StudyJavaOllamaGrenerateVo;

import java.io.InputStream;

public interface StudyJavaOllamaService {
        StudyJavaOllamaGenerateDto generate(StudyJavaOllamaGrenerateVo studyJavaOllamaGrenerateVo);
        InputStream generateStream(StudyJavaOllamaGrenerateVo studyJavaOllamaGrenerateVo);
        StudyJavaOllamaModelsDto models();
        StudyJavaOllamaVersionDto version();
        StudyJavaOllamaTagsDto tags();
        Boolean delete(StudyJavaOllamaDeleteVo studyJavaOllamaDeleteVo);
        StudyJavaOllamaPsDto ps();
        StudyJavaOllamaShowDto show();
        void pull();
}
