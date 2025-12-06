package com.studyjava.utils;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 统一错误响应类
 */
@Data
public class ErrorResponse {
    private int code;
    private String message;
    private LocalDateTime timestamp;
    private String path;

    public ErrorResponse(int code, String message, String path) {
        this.code = code;
        this.message = message;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }
}
