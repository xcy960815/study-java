package com.example.exception;


import lombok.Getter;
//import org.springframework.core.annotation.Order;

@Getter
public class StudyJavaException extends RuntimeException {

    // 获取错误码
//    private int errorCode;  // 错误码
    // 获取错误信息
    private String errorMessage;  // 错误信息

    // 构造函数
    public StudyJavaException(String errorMessage) {
        super(errorMessage);  // 调用父类的构造函数
//        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

}
