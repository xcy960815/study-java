package com.example.service;

import com.example.domain.dto.StudyJavaLoginDto;
import com.example.domain.vo.StudyJavaLoginVo;

import java.io.IOException;

public interface StudyJavaLoginService {

    StudyJavaLoginDto login(StudyJavaLoginVo studyJavaLogin);

    String getCaptcha() throws IOException;
}
