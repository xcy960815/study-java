package com.example.service;

import com.example.domain.dto.StudyJavaLoginDto;
import com.example.domain.vo.StudyJavaSysLoginVo;

import java.io.IOException;

public interface StudyJavaLoginService {

    StudyJavaSysLoginVo login(StudyJavaLoginDto studyJavaLogin);

    String getCaptcha() throws IOException;
}
