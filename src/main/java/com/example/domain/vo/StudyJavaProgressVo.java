package com.example.domain.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class StudyJavaProgressVo {
    private String id;
    private int score;
    private int level;
    private int consecutiveCorrect;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 