package com.example.exception;

import com.example.utils.ResponseGenerator;
import com.example.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    // 处理自定义错误类
    @ExceptionHandler({StudyJavaException.class})
    public ResponseEntity<ResponseResult<String>> handleStudyJavaException(StudyJavaException e) {
        // 获取错误堆栈信息
//        StackTraceElement[] stackTraceElements = e.getStackTrace();
//        StringBuilder errorDetails = new StringBuilder();
//        if (stackTraceElements.length > 0) {
//            StackTraceElement element = stackTraceElements[0]; // 获取第一个堆栈元素
//            errorDetails.append("File: ").append(element.getFileName())
//                    .append(", Line: ").append(element.getLineNumber());
//        }
//
//        // 获取请求 URL 地址
//        String requestUrl = request.getRequestURL().toString();
//
//        // 构建错误信息
//        String errorMessage = "System Error: " + e.getMessage() + ", " +
//                "At: " + errorDetails.toString() + ", " +
//                "Request URL: " + requestUrl;
//
//        log.error(errorMessage, e);
        ResponseResult<String> response = ResponseGenerator.generateErrorResult(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    // 处理post请求参数校验错误类
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ResponseResult<String>> handleMethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e, HttpServletRequest request) {
//        String errorMessage = e.getBindingResult().getFieldError().getDefaultMessage();
        // 获取所有字段的错误信息
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();

        // 如果有字段错误，获取第一个必填字段的错误信息
        String errorMessage = null;
        for (FieldError fieldError : fieldErrors) {
            if (fieldError != null) {
                errorMessage = fieldError.getDefaultMessage();
                break;  // 获取第一个错误信息后就跳出循环
            }
        }

        // 如果没有错误信息，设置默认错误信息
        if (errorMessage == null) {
            errorMessage = "请求参数错误";
        }
        ResponseResult<String> response = ResponseGenerator.generateErrorResult(errorMessage);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    // 处理get请求参数校验错误类
    @ExceptionHandler({BindException.class})
    public ResponseEntity<ResponseResult<String>> handleBindExceptionHandler(BindException e, HttpServletRequest request) {
        String errorMessage = e.getBindingResult().getFieldError().getDefaultMessage();
        ResponseResult<String> response = ResponseGenerator.generateErrorResult(errorMessage);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

//    全局异常类
    @ExceptionHandler({Exception.class})
    public ResponseEntity<ResponseResult<String>> handleException(Exception e, HttpServletRequest request) {
        // 获取错误堆栈信息
        StackTraceElement[] stackTraceElements = e.getStackTrace();
        StringBuilder errorDetails = new StringBuilder();
        if (stackTraceElements.length > 0) {
            StackTraceElement element = stackTraceElements[0]; // 获取第一个堆栈元素
            errorDetails.append("File: ").append(element.getFileName())
                    .append(", Line: ").append(element.getLineNumber());
        }

        // 获取请求 URL 地址
        String requestUrl = request.getRequestURL().toString();

        // 构建错误信息
        String errorMessage = "System Error: " + e.getMessage() + ", " +
                "At: " + errorDetails.toString() + ", " +
                "Request URL: " + requestUrl;

        log.error(errorMessage, e);
        // 生成带错误信息的响应
        ResponseResult<String> response = ResponseGenerator.generateErrorResult(errorMessage);
        return new ResponseEntity<>(response, HttpStatus.OK); // 正常返回 200
    }
}
