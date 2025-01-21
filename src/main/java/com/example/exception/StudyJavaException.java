package com.example.exception;


public class StudyJavaException extends RuntimeException {

    private int errorCode;  // 错误码
    private String errorMessage;  // 错误信息

    // 构造函数
    public StudyJavaException(int errorCode, String errorMessage) {
        super(errorMessage);  // 调用父类的构造函数
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    // 获取错误码
    public int getErrorCode() {
        return errorCode;
    }

    // 获取错误信息
    public String getErrorMessage() {
        return errorMessage;
    }
}
