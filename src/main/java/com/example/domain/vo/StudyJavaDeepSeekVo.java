package com.example.domain.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;


/**
 * deepseek 官方请求参数
 */
@Data
public class StudyJavaDeepSeekVo {

    /**
     * 会话上下文
     */
    private List<Map<String, String>> messages;
    /**
     * 要生成响应的提示
     */
    private String prompt;
    /**
     * 模型响应后的文本
     */
    private String suffix;
    /**
     * 如果设置为 false响应将作为单个响应对象返回，而不是一系列对象流
     */
    private boolean stream;

    /**
     * 模型名称
     */
    private String model;
}
