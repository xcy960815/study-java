package com.example.service;

import com.example.domain.dto.ollama.StudyJavaOllamaGenerateDto;
import com.example.domain.dto.ollama.StudyJavaOllamaModelsDto;
import com.example.domain.dto.ollama.StudyJavaOllamaTagsDto;
import com.example.domain.dto.ollama.StudyJavaOllamaVersionDto;

import java.io.InputStream;

public interface StudyJavaOllamaService {
        StudyJavaOllamaGenerateDto generate();
        InputStream generateStream();
        StudyJavaOllamaModelsDto models();
        StudyJavaOllamaVersionDto version();
        StudyJavaOllamaTagsDto tags();
        void deleteModel(String modelName);
        void ps();
}
