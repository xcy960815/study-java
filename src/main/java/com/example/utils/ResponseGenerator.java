package com.example.utils;
import com.example.domain.enums.ResponseResultEnum;

/**
 * 该类是生成响应的工具类
 */
public class ResponseGenerator {
    private static final int SUCCESS_CODE = ResponseResultEnum.SUCCESS.getCode();
    private static final int ERROR_CODE = ResponseResultEnum.ERROR.getCode();
    private static final String SUCCESS_MESSAGE = ResponseResultEnum.SUCCESS.getMessage();
    private static final String ERROR_MESSAGE = ResponseResultEnum.ERROR.getMessage();

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
    public static <T> ResponseResult<T> generateErrorResult(T message) {
        ResponseResult<T> responseResult = new ResponseResult<>();
        responseResult.setCode(ERROR_CODE);
        responseResult.setData(null);
        responseResult.setMessage(String.valueOf(message));  // Ensure message is a String
        return responseResult;
    }
}
