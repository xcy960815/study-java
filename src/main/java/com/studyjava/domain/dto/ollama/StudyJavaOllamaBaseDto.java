package com.studyjava.domain.dto.ollama;

import java.net.http.HttpRequest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.Getter;

public class StudyJavaOllamaBaseDto {
  // 静态 ObjectMapper，保证只有一个实例
  @Getter private static final ObjectMapper objectMapper = new ObjectMapper();

  static {
    objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
  }

  @JsonIgnore
  public HttpRequest.BodyPublisher getBodyPublisher() {
    try {
      // 将当前对象转换为 JSON 字符串，并构建请求体
      return HttpRequest.BodyPublishers.ofString(getObjectMapper().writeValueAsString(this));
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("Request not Body convertible.", e);
    }
  }
}
