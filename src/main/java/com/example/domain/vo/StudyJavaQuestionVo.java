package com.example.domain.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class StudyJavaQuestionVo {
    private String id;
    private int level;
    private String type;
    private String content;
    private int answer;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 