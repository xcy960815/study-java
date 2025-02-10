package com.example.controller;

import com.example.service.StudyJavaOllamaService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ollama")
public class StudyJavaOllamaController {

    @Resource
    private StudyJavaOllamaService studyJavaOllamaService;

    @GetMapping("/chat/v1")
    public String ollamaChat(@RequestParam String msg) {
        return null;
    }

}
