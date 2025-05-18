package com.example.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class StudyJavaLoginVo extends StudyJavaUserVo {
    private String token;
}
