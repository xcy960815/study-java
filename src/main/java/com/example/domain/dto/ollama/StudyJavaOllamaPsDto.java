package com.example.domain.dto.ollama;

import lombok.Data;
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

