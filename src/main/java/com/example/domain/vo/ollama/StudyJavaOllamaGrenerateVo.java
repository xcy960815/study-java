package com.example.domain.vo.ollama;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * ollama grenerate 接口 入参
 */
@Data
public class StudyJavaOllamaGrenerateVo {
    @NotBlank(message = "model不能为空")
    private String model;

    @NotBlank(message = "prompt不能为空")
    private String prompt;
    private Boolean stream;
}
