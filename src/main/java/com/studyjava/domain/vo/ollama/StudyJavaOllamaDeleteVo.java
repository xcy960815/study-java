package com.studyjava.domain.vo.ollama;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class StudyJavaOllamaDeleteVo extends StudyJavaOllamaBaseVo {
    private String name;
}
