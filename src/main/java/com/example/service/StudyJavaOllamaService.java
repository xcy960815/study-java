package com.example.service;

import com.example.domain.dto.ollama.StudyJavaOllamaModelsDto;
import com.example.domain.dto.ollama.StudyJavaOllamaTagsDto;
import com.example.domain.dto.ollama.StudyJavaOllamaVersionDto;

import java.io.InputStream;

public interface StudyJavaOllamaService {
        void generate();
        InputStream generateStream();
        StudyJavaOllamaModelsDto models();
        StudyJavaOllamaVersionDto version();
        StudyJavaOllamaTagsDto tags();
        void deleteModel(String modelName);
        void ps();
}
