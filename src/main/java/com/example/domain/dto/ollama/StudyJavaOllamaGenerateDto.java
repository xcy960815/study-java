package com.example.domain.dto.ollama;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;

/**
 * generate 接口请求参数和结果
 */
@Data
public class StudyJavaOllamaGenerateDto extends StudyJavaOllamaBaseDto {
    /**
     * 模型
     */
    @NotBlank(message = "model不能为空")
    private String model;
    
    /**
     * 问题
     */
    @NotBlank(message = "prompt不能为空")
    private String prompt;
    
    /**
     * 流
     */
    private Boolean stream;
    
    private String created_at;
    /**
     * 如果响应被流式传输，则为空，如果没有流式传输，这将包含完整的响应
     */
    private String response;
    private boolean done;
    private String done_reason;
    /**
     * 此响应中使用的对话编码，可以在下一个请求中发送，以保持对话记忆
     */
    private List<Long> context;
    /**
     * 生成响应所花费的时间
     */
    private long  total_duration;
    /**
     * 加载模型所花费的时间
     */
    private long  load_duration;
    /**
     * 提示中的令牌数量
     */
    private long  prompt_eval_count;
    /**
     * 以纳秒为单位评估提示所花费的时间
     */
    private long  prompt_eval_duration;
    /**
     * 响应中的令牌数量
     */
    private long  eval_count;
    /**
     * 以纳秒为单位的时间产生响应
     */
    private long  eval_duration;
}
