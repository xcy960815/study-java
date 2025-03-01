package com.example.controller;

import com.example.domain.dto.deepseek.StudyJavaDeepSeekModelsDto;
import com.example.domain.vo.deeseek.StudyJavaDeepSeekCompletionsVo;
import com.example.exception.StudyJavaException;
import com.example.service.StudyJavaDeepSeekService;
import com.example.utils.ResponseGenerator;
import com.example.utils.ResponseResult;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

// https://blog.csdn.net/u014390502/article/details/143275309
@RestController
@RequestMapping("/deepseek")
@Slf4j
public class StudyJavaDeepSeekController {

    @Resource
    private StudyJavaDeepSeekService studyJavaDeepSeekService;

    @PostMapping(value = "/completions",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseBodyEmitter completions(@RequestBody StudyJavaDeepSeekCompletionsVo studyJavaDeepSeekCompletionsVo ) {
        SseEmitter emitter = new SseEmitter();
        new Thread(() -> studyJavaDeepSeekService.completions(studyJavaDeepSeekCompletionsVo, emitter)).start();
        return emitter;
    }
    @GetMapping(value="/models")
    public ResponseResult<StudyJavaDeepSeekModelsDto> models() {
        try {
           return ResponseGenerator.generateSuccessResult(studyJavaDeepSeekService.models());
        } catch (IOException | InterruptedException e) {
            throw new StudyJavaException(e.getMessage());
        }
    }
}
