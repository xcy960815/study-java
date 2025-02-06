package com.example.controller;


import cn.hutool.core.date.DateUtil;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

// https://blog.csdn.net/u014390502/article/details/143275309
@RestController
@RequestMapping("/deepseek")
public class StudyJavaDeepSeekController {



    @GetMapping(value = "/query",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> serverSendEvent() {

        return Flux.interval(Duration.ofSeconds(1))
                .map(sequence -> ServerSentEvent.<String>builder()
                        .data(DateUtil.now())
                        .build())
                        .take(Duration.ofSeconds(60));  // 60 秒后自动关闭流
    };
}
