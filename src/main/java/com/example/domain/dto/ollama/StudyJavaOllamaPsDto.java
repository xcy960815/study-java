package com.example.domain.dto.ollama;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * ollama ps接口 返回数据
 */
@Data
public class StudyJavaOllamaPsDto {
    private List<model> models;
    @Data
    private static class model {
            private String name;
            private String model;
            private long size;
            private String digest;
            private Details details;
//          @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
//          @JsonDeserialize(using = LocalDateTime.class)  // 使用自定义反序列化器
//          @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX", timezone = "GMT+8")
            private String expires_at;
            private long size_vram;
                @Data
                private static class Details {
                    private String parent_model;
                    private String format;
                    private String family;
                    private List<String> families;
                    private String parameter_size;
                    private String quantization_level;
                }
    }
}

