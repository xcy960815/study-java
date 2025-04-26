package com.example.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class StudyJavaTeacherQuestionVo {
    private Long id;
    private String content;
    private String answer;
    private Integer level;
    private Long teacherId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Boolean isActive;
} 