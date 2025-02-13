package com.example.domain.dto.ollama;

import lombok.Data;
import java.util.List;

@Data
public class StudyJavaOllamaGenerateDto {
    private String model;
    private String created_at;
    private String response;
    private boolean done;
    private String done_reason;
    private List<Long> context;
    private long  total_duration;
    private long  load_duration;
    private long  prompt_eval_count;
    private long  prompt_eval_duration;
    private long  eval_count;
    private long  eval_duration;
}
