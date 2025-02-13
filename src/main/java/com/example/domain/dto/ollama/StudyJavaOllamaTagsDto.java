package com.example.domain.dto.ollama;

import lombok.Data;

import java.util.List;

@Data
public class StudyJavaOllamaTagsDto {

        private List<Model> models;
        @Data
        public static class Model {
                private String name;
                private String model;
                private String modified_at;
                private long size;
                private String digest;
                private Details details;
                @Data
                public static class Details {
                        private String parent_model;
                        private String format;
                        private String family;
                        private List<String> families;
                        private String parameter_size;
                        private String quantization_level;
                }
        }
}



