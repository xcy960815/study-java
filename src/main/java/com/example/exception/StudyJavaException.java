package com.example.exception;


import lombok.Getter;
/**
 * 自定义异常类
 */
@Getter
public class StudyJavaException extends RuntimeException {

    // 获取错误信息
    private final String errorMessage;  // 错误信息

    // 构造函数
    public StudyJavaException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }
}
