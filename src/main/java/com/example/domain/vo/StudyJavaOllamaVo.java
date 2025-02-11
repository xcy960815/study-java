package com.example.domain.vo;

import lombok.Data;
import java.util.List;
import java.util.Map;


/**
 * Ollama 请求参数
 */
@Data
public class StudyJavaOllamaVo {
    /**
     * （必需）模型名称
     */
    private String model;
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
     * 如果设置为 false ，响应将作为单个响应对象返回，而不是一系列对象流
     */
    private boolean stream;
    /**
     * 返回响应的格式。目前唯一接受的值是 json
     */
   private String format;
    /**
     * 其他模型参数，如 temperature、seed 等
     */
   private Object options;
    /**
     * 系统消息
     */
   private String system;
    /**
     * 要使用的提示模板
     */
   private String template;
    /**
     * 从先前对 /generate 的请求中返回的上下文参数，可以用于保持简短的对话记忆
     */
   private String context;
    /**
     * 如果设置为 true，将不会对提示进行任何格式化。如果您在请求API时指定了完整的模板提示，可以选择使用 raw 参数
     */
   private boolean raw;
    /**
     * 控制模型在请求后保留在内存中的时间（默认：5m）
     */
   private String keep_alive;

}
