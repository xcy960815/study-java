package com.studyjava.domain.dto.ollama;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class StudyJavaOllamaModelsDto {
  private String object;

  private List<Model> data;

  @Data
  public static class Model {
    private String id;
    private String object;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private long created;

    private String owned_by;
  }
}
