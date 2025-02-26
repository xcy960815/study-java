package com.example.domain.vo.deeseek;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class StudyJavaDeepSeekCompletionsVo extends StudyJavaDeepSeekBaseVo {
    private List<Map<String, String>> messages;
    private String model;
}
