package com.example.service.imp;

import com.example.service.StudyJavaOllamaService;
import de.asedem.Ollama;
import de.asedem.model.Model;
import de.asedem.model.ModelInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudyJavaOllamaServiceImp implements StudyJavaOllamaService {

   private Ollama ollama = Ollama.initDefault();

    @Autowired
    public List<Model>getModels(){
        return ollama.listModels();
    }

    @Autowired
    public ModelInfo getModelDetail(String modelId){
      return ollama.showInfo(modelId);
    }
}
