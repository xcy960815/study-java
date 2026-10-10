package com.studyjava.domain.dto.deepseek;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StudyJavaDeepSeekModelsDto {
  private String object;
  private List<Model> data;

  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Model {
    private String id;
    private String object;
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private long created;

    private String owned_by;
    private Long context_window;
    private Integer max_output_tokens;
    private List<String> input_modalities;
    private List<String> output_modalities;
  }
}
