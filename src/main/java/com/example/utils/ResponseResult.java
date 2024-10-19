package com.example.utils;

import java.io.Serializable;
//import lombok.Data;

/**
 * 改类是生成响应的工具类
 * @param <T>
 */

public class ResponseResult <T> implements Serializable {
    private int code;
    private String message;
    private T data;

    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
