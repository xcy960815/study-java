package com.studyjava.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.studyjava.utils.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  // 处理 Ai 系列的抛错
  @ExceptionHandler({StudyJavaAiException.class})
  public ResponseEntity<ErrorResponse> handleStudyJavaOllamaException(
      StudyJavaAiException error, HttpServletRequest request) {
    log.error("AI Service Error: {}", error.getErrorMessage(), error);
    return new ResponseEntity<>(
        new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            error.getErrorMessage(),
            request.getRequestURI()),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  // 处理自定义错误类
  @ExceptionHandler({StudyJavaException.class})
  public ResponseEntity<ErrorResponse> handleStudyJavaException(
      StudyJavaException e, HttpServletRequest request) {
    log.error("Business Exception: {}", e.getMessage(), e);
    return new ResponseEntity<>(
        new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage(), request.getRequestURI()),
        HttpStatus.BAD_REQUEST);
  }

  // 处理post请求参数校验错误类
  @ExceptionHandler({MethodArgumentNotValidException.class})
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidExceptionHandler(
      MethodArgumentNotValidException e, HttpServletRequest request) {
    String errorMessage = resolveErrorMessage(e);
    return new ResponseEntity<>(
        new ErrorResponse(HttpStatus.BAD_REQUEST.value(), errorMessage, request.getRequestURI()),
        HttpStatus.BAD_REQUEST);
  }

  // 处理get请求参数校验错误类
  @ExceptionHandler({BindException.class})
  public ResponseEntity<ErrorResponse> handleBindExceptionHandler(
      BindException e, HttpServletRequest request) {
    String errorMessage = resolveErrorMessage(e);
    return new ResponseEntity<>(
        new ErrorResponse(HttpStatus.BAD_REQUEST.value(), errorMessage, request.getRequestURI()),
        HttpStatus.BAD_REQUEST);
  }

  /** 字段级错误可能不存在（如类级约束），避免在异常处理器中再抛 NPE */
  private String resolveErrorMessage(BindException e) {
    FieldError fieldError = e.getFieldError();
    return fieldError != null ? fieldError.getDefaultMessage() : e.getMessage();
  }

  // 全局异常类
  @ExceptionHandler({Exception.class})
  public ResponseEntity<ErrorResponse> handleException(Exception e, HttpServletRequest request) {
    log.error("System Error, Request URL: {}", request.getRequestURL(), e);

    return new ResponseEntity<>(
        new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(), "系统异常，请稍后重试", request.getRequestURI()),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
