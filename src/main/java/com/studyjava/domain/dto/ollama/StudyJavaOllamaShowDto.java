package com.studyjava.domain.dto.ollama;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/** ollama show 接口 返回数据 */
@Data
// @JsonIgnoreProperties(ignoreUnknown = true) //Jackson 忽略 JSON 中未在 Java 类中声明的字段，而不会引发解析错误。
public class StudyJavaOllamaShowDto {

  private String license;
  private String modelfile;
  private String parameters;
  private String template;
  private StudyJavaOllamaTagsDto.Model.Details details;
  private ModleInfo model_info;
  private String modified_at;

  @Data
  @JsonIgnoreProperties(ignoreUnknown = true) // Jackson 忽略 JSON 中未在 Java 类中声明的字段，而不会引发解析错误。
  public static class ModleInfo {
    // General fields
    @JsonProperty("general.architecture")
    private String generalArchitecture;

    @JsonProperty("general.basename")
    private String generalBasename;

    @JsonProperty("general.file_type")
    private int generalFileType;

    @JsonProperty("general.parameter_count")
    private long generalParameterCount;

    @JsonProperty("general.quantization_version")
    private int generalQuantizationVersion;

    @JsonProperty("general.size_label")
    private String generalSizeLabel;

    @JsonProperty("general.type")
    private String generalType;

    @JsonProperty("general.version")
    private String generalVersion;

    // Qwen2 fields
    @JsonProperty("qwen2.attention.head_count")
    private String qwen2AttentionHeadCount;

    @JsonProperty("qwen2.attention.head_count_kv")
    private String qwen2AttentionHeadCountKv;

    @JsonProperty("qwen2.attention.layer_norm_rms_epsilon")
    private String qwen2AttentionLayerNormRmsEpsilon;

    @JsonProperty("qwen2.block_count")
    private String qwen2BlockCount;

    @JsonProperty("qwen2.context_length")
    private String qwen2ContextLength;

    @JsonProperty("qwen2.embedding_length")
    private String qwen2EmbeddingLength;

    @JsonProperty("qwen2.feed_forward_length")
    private String qwen2FeedForwardLength;

    @JsonProperty("qwen2.rope.freq_base")
    private String qwen2RopeFreqBase;

    // Tokenizer fields
    @JsonProperty("tokenizer.ggml.add_bos_token")
    private String tokenizerGgmlAddBosToken;

    @JsonProperty("tokenizer.ggml.add_eos_token")
    private String tokenizerGgmlAddEosToken;

    @JsonProperty("tokenizer.ggml.bos_token_id")
    private String tokenizerGgmlBosTokenId;

    @JsonProperty("tokenizer.ggml.eos_token_id")
    private String tokenizerGgmlEosTokenId;

    @JsonProperty("tokenizer.ggml.merges")
    private String tokenizerGgmlMerges;

    @JsonProperty("tokenizer.ggml.model")
    private String tokenizerGgmlModel;

    @JsonProperty("tokenizer.ggml.padding_token_id")
    private String tokenizerGgmlPaddingTokenId;

    @JsonProperty("tokenizer.ggml.pre")
    private String tokenizerGgmlPre;

    @JsonProperty("tokenizer.ggml.token_type")
    private String tokenizerGgmlTokenType;

    @JsonProperty("tokenizer.ggml.tokens")
    private String tokenizerGgmlTokens;
  }
}
