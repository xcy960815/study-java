package com.studyjava.exception;

import com.studyjava.utils.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 处理 Ai 系列的抛错
    @ExceptionHandler({StudyJavaAiException.class})
    public ResponseEntity<ErrorResponse> handleStudyJavaOllamaException(StudyJavaAiException error, HttpServletRequest request) {
        log.error("AI Service Error: {}", error.getErrorMessage(), error);
        return new ResponseEntity<>(
            new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), error.getErrorMessage(), request.getRequestURI()), 
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    // 处理自定义错误类
    @ExceptionHandler({StudyJavaException.class})
    public ResponseEntity<ErrorResponse> handleStudyJavaException(StudyJavaException e, HttpServletRequest request) {
        log.error("Business Exception: {}", e.getMessage(), e);
        return new ResponseEntity<>(
            new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage(), request.getRequestURI()), 
            HttpStatus.BAD_REQUEST
        );
    }

    // 处理post请求参数校验错误类
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e, HttpServletRequest request) {
        String errorMessage = e.getBindingResult().getFieldError().getDefaultMessage();
        return new ResponseEntity<>(
            new ErrorResponse(HttpStatus.BAD_REQUEST.value(), errorMessage, request.getRequestURI()), 
            HttpStatus.BAD_REQUEST
        );
    }

    // 处理get请求参数校验错误类
    @ExceptionHandler({BindException.class})
    public ResponseEntity<ErrorResponse> handleBindExceptionHandler(BindException e, HttpServletRequest request) {
        String errorMessage = e.getBindingResult().getFieldError().getDefaultMessage();
        return new ResponseEntity<>(
            new ErrorResponse(HttpStatus.BAD_REQUEST.value(), errorMessage, request.getRequestURI()), 
            HttpStatus.BAD_REQUEST
        );
    }

    // 全局异常类
    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> handleException(Exception e, HttpServletRequest request) {
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
                "At: " + errorDetails + ", " +
                "Request URL: " + requestUrl;

        log.error(errorMessage, e);
        
        // 生产环境建议隐藏详细堆栈信息，这里为了调试保留
        return new ResponseEntity<>(
            new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMessage, request.getRequestURI()), 
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
