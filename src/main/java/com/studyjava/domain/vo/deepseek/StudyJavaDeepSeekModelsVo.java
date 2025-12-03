package com.studyjava.domain.vo.deepseek;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.List;

/**
 * DeepSeek AI 模型列表响应
 */
@Data
public class StudyJavaDeepSeekModelsVo {
    private String object;
    private List<Model> data;

    @Data
    public static class Model {
        private String id;
        private String object;
        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
        private long created;
        private String owned_by;
    }
}
