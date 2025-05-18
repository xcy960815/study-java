package com.example.service;

import com.example.domain.dto.StudyJavaLoginDto;
import com.example.domain.vo.StudyJavaLoginVo;

import java.io.IOException;

public interface StudyJavaLoginService {

    StudyJavaLoginVo login(StudyJavaLoginDto studyJavaLogin);

    String getCaptcha() throws IOException;
}
