package com.example.controller;

import com.example.domain.dto.ollama.StudyJavaOllamaTagsDto;
import com.example.domain.dto.ollama.StudyJavaOllamaVersionDto;
import com.example.domain.dto.ollama.StudyJavaOllamaModelsDto;
import com.example.service.StudyJavaOllamaService;
import com.example.utils.ResponseGenerator;
import com.example.utils.ResponseResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ollama")
public class StudyJavaOllamaController {

    @Resource
    private StudyJavaOllamaService studyJavaOllamaService;

    /**
     * 获取标签
     */
    @GetMapping("/tags")
    public ResponseResult<StudyJavaOllamaTagsDto> tags() {
        return ResponseGenerator.generateSuccessResult(studyJavaOllamaService.tags());
    }

    /**
     *
     */
    @GetMapping("/models")
    public ResponseResult<StudyJavaOllamaModelsDto> models() {
       return ResponseGenerator.generateSuccessResult(studyJavaOllamaService.models());
    }

    /**
     * 获取ollama版本
     * @return ResponseResult
     */
    @GetMapping("/version")
    public ResponseResult<StudyJavaOllamaVersionDto> version() {
        return ResponseGenerator.generateSuccessResult(studyJavaOllamaService.version());
    }

}
