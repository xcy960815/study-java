package com.studyjava.domain.vo.ollama;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class StudyJavaOllamaPullVo extends StudyJavaOllamaBaseVo {
  private String name;
  private Boolean insecure;
  private Boolean stream;
}
