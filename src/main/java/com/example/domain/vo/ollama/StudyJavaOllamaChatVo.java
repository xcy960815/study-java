package com.example.domain.vo.ollama;

import lombok.Data;

import java.util.List;

@Data
public class StudyJavaOllamaChatVo extends StudyJavaOllamaBaseVo {
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
