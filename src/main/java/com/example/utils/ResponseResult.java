package com.example.utils;

import lombok.Data;

import java.io.Serializable;



/**
 * 该类是生成响应的工具类
 * @param <T>
 */
@Data
public class ResponseResult<T> implements Serializable {
    private int code ;
    private String message;
    private T data;
}
