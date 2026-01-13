package com.studyjava.domain.vo.ollama;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class StudyJavaOllamaCompletionsVo extends StudyJavaOllamaBaseVo {
  private String model;
  private List<Message> messages;
  private Boolean stream;

  @Data
  public static class Message {
    private String role;
    private String content;
    private List<String> images;
    private List<String> tool_calls;
  }
}
