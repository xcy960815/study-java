package com.studyjava.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseController {

    /**
     * 开启 MyBatis Plus 分页
     *
     * @param pageNum  页码
     * @param pageSize 每页大小
     * @param <T>      泛型
     * @return IPage<T>
     */
    protected <T> Page<T> startPage(int pageNum, int pageSize) {
        return new Page<>(pageNum, pageSize);
    }

    /**
     * 封装分页返回数据
     *
     * @param page 分页对象
     * @return 返回格式化后的分页数据
     */
    protected <T> Map<String, Object> getPageData(IPage<T> page) {
        Map<String, Object> result = new HashMap<>();
        result.put("data", page.getRecords());
        result.put("total", page.getTotal());
        return result;
    }
}
