package com.example.controller;

import cn.hutool.http.HttpResponse;
import com.example.service.StudyJavaDeepSeekService;
import io.jsonwebtoken.io.IOException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
//import reactor.core.publisher.Flux;

import java.io.InputStream;

// https://blog.csdn.net/u014390502/article/details/143275309
@RestController
@RequestMapping("/deepseek")
@Slf4j
public class StudyJavaDeepSeekController {

    @Resource
    private StudyJavaDeepSeekService studyJavaDeepSeekService;

    @PostMapping(value = "/v1/chat/completions",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<StreamingResponseBody> completions() {
        // 获取 Service 返回的 HttpResponse
        HttpResponse response = studyJavaDeepSeekService.completions();
        // 创建 StreamingResponseBody 来逐步返回流
        StreamingResponseBody streamingResponseBody = out -> {
            try (InputStream inputStream = response.bodyStream()) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                    out.flush();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        // 设置响应头
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "text/event-stream"); // 使用 text/event-stream
        return new ResponseEntity<>(streamingResponseBody, headers, HttpStatus.OK);
    }
}
