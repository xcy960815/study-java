package com.example.domain.dto;

import lombok.Data;

@Data
public class StudyJavaAiErrorDto {

    private Error error;
    @Data
    public static class Error {
        private String message;
        private String type;
        private Object param;
        private String code;
    }
}
