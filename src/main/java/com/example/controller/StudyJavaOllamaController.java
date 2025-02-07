package com.example.controller;


import com.example.service.StudyJavaOllamaService;
import com.example.utils.ResponseGenerator;
import com.example.utils.ResponseResult;
import de.asedem.model.Model;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ollama")
public class StudyJavaOllamaController {
    @Resource
    private StudyJavaOllamaService studyJavaOllamaService;


    @GetMapping("/getModels")
    public ResponseResult<List<Model>> getModels() {
        return ResponseGenerator.generateSuccessResult(studyJavaOllamaService.getModels()) ;
    }

}
