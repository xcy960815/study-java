package com.studyjava.service;

import com.studyjava.domain.dto.StudyJavaLoginDto;
import com.studyjava.domain.vo.StudyJavaSysLoginVo;

import java.io.IOException;

public interface StudyJavaLoginService {

    StudyJavaSysLoginVo login(StudyJavaLoginDto studyJavaLogin);

    String getCaptcha() throws IOException;

    StudyJavaSysLoginVo refreshToken(String refreshToken);

    void logout();
}
