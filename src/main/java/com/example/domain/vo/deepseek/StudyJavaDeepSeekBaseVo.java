package com.example.domain.vo.deepseek;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.net.http.HttpRequest;

/**
 * DeepSeek AI 请求基类
 */
@Data
public class StudyJavaDeepSeekBaseVo {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 获取请求体
     * @return HttpRequest.BodyPublisher
     */
    public HttpRequest.BodyPublisher getBodyPublisher() {
        try {
            String json = objectMapper.writeValueAsString(this);
            return HttpRequest.BodyPublishers.ofString(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON序列化失败", e);
        }
    }
} 