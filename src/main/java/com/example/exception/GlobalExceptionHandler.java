package com.example.exception;

import com.example.utils.ResponseGenerator;
import com.example.utils.ResponseResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;


/**
 * 全局错误捕获
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseResult<String>> handleException(Exception e) {
        // 生成一个带错误信息的响应，返回 HTTP 200
        ResponseResult<String> response = ResponseGenerator.generatErrorResult("系统错误: " + e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.OK);  // 返回 200 OK
    }

    @ExceptionHandler(StudyJavaException.class)
    public ResponseEntity<ResponseResult<String>> handleCustomException(StudyJavaException e) {
        // 生成一个带自定义错误信息的响应，返回 HTTP 200
        ResponseResult<String> response = ResponseGenerator.generatErrorResult("自定义错误: " + e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.OK);  // 返回 200 OK
    }
}
