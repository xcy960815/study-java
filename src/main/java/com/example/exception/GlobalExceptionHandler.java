package com.example.exception;

import com.example.utils.ResponseGenerator;
import com.example.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import jakarta.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(StudyJavaException.class)
    public ResponseEntity<ResponseResult<String>> handleCustomException(StudyJavaException e) {
        // 生成一个带自定义错误信息的响应，返回 HTTP 200
        ResponseResult<String> response = ResponseGenerator.generateErrorResult(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.OK);  // 返回 200 OK
    }

//    https://blog.csdn.net/weixin_44847332/article/details/123457771
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ResponseResult<String>> handleMethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e, HttpServletRequest request) {
        // 生成带错误信息的响应
        ResponseResult<String> response = ResponseGenerator.generateErrorResult(e.getBindingResult().getFieldError().getDefaultMessage());
        return new ResponseEntity<>(response, HttpStatus.OK); // 正常返回 200
    }

//    @ExceptionHandler({BindException.class})
//    public ResponseEntity<ResponseResult<String>> handleBindExceptionHandler(MethodArgumentNotValidException e, HttpServletRequest request) {
//        // 生成带错误信息的响应
//        ResponseResult<String> response = ResponseGenerator.generateErrorResult(e.getBindingResult().getFieldError().getDefaultMessage());
//        return new ResponseEntity<>(response, HttpStatus.OK); // 正常返回 200
//    }

    @ExceptionHandler(Exception.class)
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
