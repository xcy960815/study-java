package com.example.domain.vo.ollama;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ollama grenerate 接口 入参
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StudyJavaOllamaGrenerateVo extends  StudyJavaOllamaBaseVo {
    /**
     * 模型
     */
    @NotBlank(message = "model不能为空")
    private String model;
    /**
     * 问题
     */
    @NotBlank(message = "prompt不能为空")
    private String prompt;
    /**
     * 流
     */
    private Boolean stream;

}
