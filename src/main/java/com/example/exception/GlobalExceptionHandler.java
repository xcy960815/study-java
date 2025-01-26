package com.example.exception;

import com.example.utils.ResponseGenerator;
import com.example.utils.ResponseResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    //    @ExceptionHandler(Exception.class)
    //    public ResponseEntity<ResponseResult<String>> handleException(Exception e) {
    //        // 生成一个带错误信息的响应，返回 HTTP 200
    //        ResponseResult<String> response = ResponseGenerator.generateErrorResult("系统错误: " + e.getMessage());
    //        return new ResponseEntity<>(response, HttpStatus.OK);  // 返回 200 OK
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

        // 生成带错误信息的响应
        ResponseResult<String> response = ResponseGenerator.generateErrorResult(errorMessage);
        return new ResponseEntity<>(response, HttpStatus.OK); // 返回 500
    }

//    @ExceptionHandler(StudyJavaException.class)
//    public ResponseEntity<ResponseResult<String>> handleCustomException(StudyJavaException e, HttpServletRequest request) {
//        // 获取错误堆栈信息
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
//        String errorMessage = "Custom Error: " + e.getMessage() + ", " +
//                "At: " + errorDetails.toString() + ", " +
//                "Request URL: " + requestUrl;
//
//        // 生成带自定义错误信息的响应
//        ResponseResult<String> response = ResponseGenerator.generateErrorResult(errorMessage);
//        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR); // 返回 500
//    }
        @ExceptionHandler(StudyJavaException.class)
        public ResponseEntity<ResponseResult<String>> handleCustomException(StudyJavaException e) {
            // 生成一个带自定义错误信息的响应，返回 HTTP 200
            ResponseResult<String> response = ResponseGenerator.generateErrorResult(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.OK);  // 返回 200 OK
        }
}
