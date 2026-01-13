package com.studyjava.domain.dto.deepseek;

import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/** DeepSeek AI 对话请求参数 */
@Data
public class StudyJavaDeepSeekCompletionsRequestDto {
  /** 消息列表 */
  @NotEmpty(message = "消息列表不能为空")
  private List<Map<String, String>> messages;

  /** 模型 */
  @NotBlank(message = "模型不能为空")
  private String model;

  /** 流式输出 */
  private Boolean stream;

  /** 指定最大输出长度 */
  private Integer max_tokens;

  /** 指定温度 */
  private Double temperature;

  /** 指定top_p */
  private Double top_p;

  /** 指定n */
  private Integer n;

  /** 指定stop */
  private List<String> stop;

  /** 指定logit_bias */
  private Map<String, Double> logit_bias;

  /** 指定presence_penalty */
  private Double presence_penalty;

  /** 指定frequency_penalty */
  private Double frequency_penalty;

  /** 指定user */
  private String user;
}
