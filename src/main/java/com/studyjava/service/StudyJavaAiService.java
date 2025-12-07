package com.studyjava.service;

import com.studyjava.domain.dto.StudyJavaCompletionsErrorDto;
//import com.example.domain.dto.StudyJavaCompletionsDto;
import com.studyjava.exception.StudyJavaAiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import com.studyjava.exception.StudyJavaException;

@Slf4j
public abstract class StudyJavaAiService {
    /**
     * 数据前缀
     */
    protected static final String DATA_PREFIX = "data: ";

    /**
     * keep-alive
     */
    protected static final String KEEPALIVE_FLAG = " : keep-alive";
    /**
     * object mapper
     */
    protected static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * http client
     */
    protected final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(30))
            .build();

    /**
     * 通用响应处理 (非流式)
     * @param response HttpResponse
     * @param responseType Class
     * @return T
     */
    protected <T> T handleResponse(HttpResponse<String> response, Class<T> responseType) throws IOException {
        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), responseType);
        } else {
            // 尝试解析错误信息
            try {
                StudyJavaCompletionsErrorDto errorDto = objectMapper.readValue(response.body(), StudyJavaCompletionsErrorDto.class);
                throw new StudyJavaAiException(errorDto.getError().getCode(), errorDto.getError().getMessage());
            } catch (Exception e) {
                if (e instanceof StudyJavaAiException) {
                    throw (StudyJavaAiException) e;
                }
                // 如果解析失败，抛出通用异常
                throw new RuntimeException("请求失败，状态码：" + response.statusCode() + "，内容：" + response.body());
            }
        }
    }

    protected void readResponseLines(HttpResponse<InputStream> response, SseEmitter emitter) throws IOException {
        int statusCode = response.statusCode();
        if (statusCode != 200) {
            try {
                StudyJavaCompletionsErrorDto studyJavaCompletionsErrorDto = objectMapper.readValue(response.body(), StudyJavaCompletionsErrorDto.class);
                throw new StudyJavaAiException(studyJavaCompletionsErrorDto.getError().getCode(), studyJavaCompletionsErrorDto.getError().getMessage());
            } catch (Exception e) {
                if (e instanceof StudyJavaAiException) {
                    throw (StudyJavaAiException) e;
                }
                throw new RuntimeException("请求失败，状态码：" + statusCode);
            }
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.body(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null ) {
                if(!line.trim().isEmpty() && line.startsWith(DATA_PREFIX) && !line.startsWith(KEEPALIVE_FLAG)) {
                    String responseLine = line.substring(DATA_PREFIX.length());
                    // 过滤掉 [DONE]
                    if ("[DONE]".equals(responseLine.trim())) {
                        continue;
                    }
                    emitter.send(responseLine);
                }
            }
        }
    }
}
