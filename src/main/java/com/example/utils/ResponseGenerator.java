package com.example.utils;


import jakarta.annotation.Resource;

/**
 * 改类是生成响应的工具类
 */
public class ResponseGenerator {
    private static final int SUCCESS_CODE = 200;
    private static final int ERROR_CODE = 500;
    private static final String SUCCESS_MESSAGE = "SUCCESS";
    private static final String ERROR_MESSAGE = "ERROR";

//    @Resource
//    ResponseResult responseResult;
    /**
     * 生成响应
     * @param code
     * @param message
     * @param data
     * @return ResponseResult
     */
    public static <T> ResponseResult<T> generateResponseResult(int code, String message, T data) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    /**
     * 生成成功响应
     * @param data
     * @return ResponseResult
     */
    public static ResponseResult generatSuccessResult(Object data) {
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(SUCCESS_CODE);
        responseResult.setMessage(SUCCESS_MESSAGE);
        responseResult.setData(data);
        return responseResult;
    }

    /**
     * 生成失败响应
     * @param message
     * @return ResponseResult
     */
    public static ResponseResult generatErrorResult(String message) {
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(ERROR_CODE);
        responseResult.setMessage(message);
        return responseResult;
    }
}
