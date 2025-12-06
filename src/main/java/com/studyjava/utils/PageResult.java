package com.studyjava.utils;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果封装类
 * @param <T> 数据类型
 */
@Data
public class PageResult<T> implements Serializable {
    private List<T> data;
    private long total;

    public static <T> PageResult<T> of(List<T> data, long total) {
        PageResult<T> result = new PageResult<>();
        result.setData(data);
        result.setTotal(total);
        return result;
    }
}
