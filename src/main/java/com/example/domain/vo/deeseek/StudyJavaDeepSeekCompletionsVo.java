package com.example.domain.vo.deeseek;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class StudyJavaDeepSeekCompletionsVo extends StudyJavaDeepSeekBaseVo {
    /**
     * 消息列表
     */
    private List<Map<String, String>> messages;
    /**
     * 模型
     */
    private String model;
    /**
     * 流式输出
     *
     */
    private Boolean stream;

    /**
     * 指定最大输出长度
     */
    private Integer max_tokens;

    /**
     * 指定温度
     */
    private Double temperature;

    /**
     * 指定top_p
     */
    private Double top_p;

    /**
     * 指定n
     */
    private Integer n;

    /**
     * 指定stop
     */
    private List<String> stop;

    /**
     * 指定logit_bias
     */
    private Map<String, Double> logit_bias;

    /**
     * 指定presence_penalty
     */
    private Double presence_penalty;

    /**
     * 指定frequency_penalty
     */
    private Double frequency_penalty;

    /**
     * 指定user
     */
    private String user;
}
