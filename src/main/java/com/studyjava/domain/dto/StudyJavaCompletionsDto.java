package com.studyjava.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class StudyJavaCompletionsDto {
    private String id;
    private String object;
    private int created;
    private String model;
    private String system_fingerprint;
    private List<StudyJavaOllamaCompletionsChoiceDto> choices;

    @Data
    static class StudyJavaOllamaCompletionsChoiceDto {
        private int index;
        private StudyJavaOllamaCompletionsDeltaDto delta;
        private String finish_reason;

        @Data
        static class StudyJavaOllamaCompletionsDeltaDto {
            private String role;
            private String content;
        }
    }
}
