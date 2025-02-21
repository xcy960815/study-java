package com.example.service;

import com.example.domain.dto.ollama.*;
import com.example.domain.vo.ollama.StudyJavaOllamaDeleteVo;
import com.example.domain.vo.ollama.StudyJavaOllamaGrenerateVo;
import com.example.domain.vo.ollama.StudyJavaOllamaShowVo;
import java.io.IOException;

public interface StudyJavaOllamaService {
        StudyJavaOllamaGenerateDto generate(StudyJavaOllamaGrenerateVo studyJavaOllamaGrenerateVo) throws IOException, InterruptedException;
        void generateStream(StudyJavaOllamaGrenerateVo studyJavaOllamaGrenerateVo, DataCallback callback) throws IOException,InterruptedException;
        StudyJavaOllamaModelsDto models() throws IOException, InterruptedException;;
        StudyJavaOllamaVersionDto version() throws IOException, InterruptedException;
        StudyJavaOllamaTagsDto tags() throws IOException, InterruptedException;;
        Boolean delete(StudyJavaOllamaDeleteVo studyJavaOllamaDeleteVo) throws IOException, InterruptedException;
        StudyJavaOllamaPsDto ps() throws IOException, InterruptedException;
        StudyJavaOllamaShowDto show(StudyJavaOllamaShowVo studyJavaOllamaShowVo) throws IOException, InterruptedException;
//        void pull();
}
