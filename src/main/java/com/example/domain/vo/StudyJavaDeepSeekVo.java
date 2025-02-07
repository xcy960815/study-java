package com.example.domain.vo;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class StudyJavaDeepSeekVo {
    private String model;
    private List<Map<String, String>> messages;
    private boolean stream;

}
