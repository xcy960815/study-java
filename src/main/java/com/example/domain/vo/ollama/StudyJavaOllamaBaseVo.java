package com.example.domain.vo.ollama;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Getter;
import java.net.http.HttpRequest;

public class StudyJavaOllamaBaseVo {
    // 返回 ObjectMapper 实例
    // 静态 ObjectMapper，保证只有一个实例
    @Getter
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        // 配置 ObjectMapper（如需要）
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // 格式化输出
        // 其他配置可以加在这里
        // objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @JsonIgnore
    public HttpRequest.BodyPublisher getBodyPublisher(){
        try {
            // 将当前对象转换为 JSON 字符串，并构建请求体
            return HttpRequest.BodyPublishers.ofString(getObjectMapper().writeValueAsString(this));
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Request not Body convertible.", e);
        }
    }
}
