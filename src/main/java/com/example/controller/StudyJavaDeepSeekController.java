package com.example.controller;

import com.example.domain.vo.deeseek.StudyJavaDeepSeekCompletionsVo;
import com.example.service.StudyJavaDeepSeekService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.HashMap;


// https://blog.csdn.net/u014390502/article/details/143275309
@RestController
//@RequestMapping("/deepseek")
@Slf4j
public class StudyJavaDeepSeekController {

    @Resource
    private StudyJavaDeepSeekService studyJavaDeepSeekService;

    @PostMapping(value = "/v1/chat/completions",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseBodyEmitter completions(@RequestBody StudyJavaDeepSeekCompletionsVo studyJavaDeepSeekCompletionsVo ) {
        SseEmitter emitter = new SseEmitter();
        new Thread(() -> studyJavaDeepSeekService.completions(studyJavaDeepSeekCompletionsVo, emitter)).start();
        return emitter;
    }
}
