package com.example.domain.vo.ollama;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StudyJavaOllamaShowVo extends StudyJavaOllamaBaseVo {
    @NotBlank(message = "name不能为空")
    String name;
}
