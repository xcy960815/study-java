package com.example.utils;

import java.io.Serializable;
import lombok.Data;

/**
 * 改类是生成响应的工具类
 * @param <T>
 */
@Data
public class ResponseResult <T> implements Serializable {
    private int code;
    private String message;
    private T data;
}
