package com.example.exception;

import lombok.Getter;

@Getter
public class StudyJavaAiException extends RuntimeException {
    // 获取错误信息
    private final String errorMessage;  // 错误信息
    // 错误状态码
    private final String statusCode;
    public StudyJavaAiException(String statusCode , String errorMessage) {
        super(errorMessage);
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
    }
}
