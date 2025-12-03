package com.studyjava.domain.vo.ollama;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class StudyJavaOllamaCompletionsVo extends StudyJavaOllamaBaseVo {
    private String model;
    private List<Message> messages;
    private Boolean stream;

    @Data
    static public class Message {
        private String role;
        private String content;
        private List<String> images;
        private List<String> tool_calls;
    }
}
