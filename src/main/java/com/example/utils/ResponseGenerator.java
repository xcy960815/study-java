package com.example.utils;


/**
 * 改类是生成响应的工具类
 */
public class ResponseGenerator {
    private static final int SUCCESS_CODE = 200;
    private static final int ERROR_CODE = 500;
    private static final String SUCCESS_MESSAGE = "SUCCESS";
//    private static final String ERROR_MESSAGE = "ERROR";

    /*
     * 生成响应
     * code
     * message
     * data
     * @return ResponseResult
     */
//    public static <T> ResponseResult<T> generateResponseResult(int code, String message, T data) {
//        ResponseResult<T> result = new ResponseResult<>();
//        result.setCode(code);
//        result.setMessage(message);
//        result.setData(data);
//        return result;
//    }

    /**
     * 生成成功响应
     * data
     * @return ResponseResult
     */
//    public static ResponseResult generateSuccessResult(Object data) {
//        ResponseResult responseResult = new ResponseResult();
//        responseResult.setCode(SUCCESS_CODE);
//        responseResult.setMessage(SUCCESS_MESSAGE);
//        responseResult.setData(data);
//        return responseResult;
//    }
    public static <T> ResponseResult<T> generateSuccessResult(T data) {
        // 创建一个 ResponseResult 对象，指定泛型类型 T
        ResponseResult<T> responseResult = new ResponseResult<>();
        responseResult.setCode(SUCCESS_CODE);
        responseResult.setMessage(SUCCESS_MESSAGE);
        responseResult.setData(data);
        return responseResult;
    }

    /**
     * 生成失败响应
     * message
     * @return ResponseResult
     */
//    public static <String> ResponseResult<String> generateErrorResult(String message) {
//        ResponseResult<String> responseResult = new ResponseResult<>();
//        responseResult.setCode(ERROR_CODE);
//        responseResult.setMessage(message);
//        return responseResult;
//    }
    public static <T> ResponseResult<T> generateErrorResult(T message) {
        ResponseResult<T> responseResult = new ResponseResult<>();
        responseResult.setCode(ERROR_CODE);
        responseResult.setMessage(String.valueOf(message));  // Ensure message is a String
        return responseResult;
    }
}
