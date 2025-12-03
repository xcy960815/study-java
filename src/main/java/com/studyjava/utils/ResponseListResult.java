package com.studyjava.utils;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * 该类是生成list响应的工具类
 * @param <T>
 */
@Data
public class ResponseListResult<T> implements Serializable {
    /**
     * 状态码
     */
    private int code;
    /**
     * 消息
     */
    private String message;
    /**
     * 数据包装器
     */
    private DataWrapper<T> data;

    @Data
    public static class DataWrapper<T> implements Serializable {
        /**
         * 数据列表
         */
        private List<T> data;
        /**
         * 总数
         */
        private long total;
    }
}



