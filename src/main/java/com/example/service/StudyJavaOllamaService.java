package com.example.service;

import de.asedem.model.Model;
import de.asedem.model.ModelInfo;

import java.util.List;


public interface StudyJavaOllamaService {
        public List<Model>getModels();

        public ModelInfo getModelDetail(String modelName);

        public String generate();
}
