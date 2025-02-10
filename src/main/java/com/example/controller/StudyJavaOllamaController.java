package com.example.controller;

import com.example.service.StudyJavaOllamaService;
import com.example.utils.ResponseGenerator;
import com.example.utils.ResponseResult;
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



    @GetMapping("/tags")
    public ResponseResult<List<Object>> tags() {
        return ResponseGenerator.generateSuccessResult(studyJavaOllamaService.tags());
    }

    /**
     * 获取ollama版本
     * @return ResponseResult
     */
    @GetMapping("/version")
    public ResponseResult<String> version() {
        return ResponseGenerator.generateSuccessResult(studyJavaOllamaService.version());
    }

}
