package com.studyjava.domain.vo.deepseek;

import java.net.http.HttpRequest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

/** DeepSeek AI 请求基类 */
@Data
public class StudyJavaDeepSeekBaseVo {
  private static final ObjectMapper objectMapper = new ObjectMapper();

  /**
   * 获取请求体
   *
   * @return HttpRequest.BodyPublisher
   */
  @JsonIgnore
  public HttpRequest.BodyPublisher getBodyPublisher() {
    try {
      String json = objectMapper.writeValueAsString(this);
      return HttpRequest.BodyPublishers.ofString(json);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("JSON序列化失败", e);
    }
  }
}
