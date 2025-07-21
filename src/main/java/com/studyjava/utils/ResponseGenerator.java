package com.studyjava.utils;

import com.studyjava.domain.enums.ResponseResultEnum;
import java.util.List;

/**
 * 响应生成工具类
 * 用于统一生成API响应结果
 */
public class ResponseGenerator {
    private static final int SUCCESS_CODE = ResponseResultEnum.SUCCESS.getCode();
    private static final int ERROR_CODE = ResponseResultEnum.ERROR.getCode();
    private static final String SUCCESS_MESSAGE = ResponseResultEnum.SUCCESS.getMessage();
    private static final String ERROR_MESSAGE = ResponseResultEnum.ERROR.getMessage();

    private ResponseGenerator() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 生成基础响应对象
     * @param code 状态码
     * @param message 消息
     * @param data 数据
     * @return ResponseResult
     */
    private static <T> ResponseResult<T> createResponse(int code, String message, T data) {
        ResponseResult<T> response = new ResponseResult<>();
        response.setCode(code);
        response.setMessage(message);
        response.setData(data);
        return response;
    }

    /**
     * 生成成功响应
     * @param data 响应数据
     * @return ResponseResult
     */
    public static <T> ResponseResult<T> generateSuccessResult(T data) {
        return createResponse(SUCCESS_CODE, SUCCESS_MESSAGE, data);
    }

    /**
     * 生成无数据的成功响应
     * @return ResponseResult
     */
    public static <T> ResponseResult<T> generateSuccessResult() {
        return generateSuccessResult(null);
    }

    /**
     * 生成错误响应
     * @param message 错误信息
     * @return ResponseResult
     */
    public static <T> ResponseResult<T> generateErrorResult(T message) {
        return createResponse(ERROR_CODE, String.valueOf(message), null);
    }

    /**
     * 生成列表响应
     * @param data 数据列表
     * @param total 总数
     * @return ResponseListResult
     */
    public static <T> ResponseListResult<T> generateListResult(List<T> data, long total) {
        ResponseListResult<T> response = new ResponseListResult<>();
        response.setCode(SUCCESS_CODE);
        response.setMessage(SUCCESS_MESSAGE);

        ResponseListResult.DataWrapper<T> wrapper = new ResponseListResult.DataWrapper<>();
        wrapper.setData(data);
        wrapper.setTotal(total);
        response.setData(wrapper);

        return response;
    }
}
