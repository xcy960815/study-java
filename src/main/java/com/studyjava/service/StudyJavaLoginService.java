package com.studyjava.service;

import java.io.IOException;

import com.studyjava.domain.dto.StudyJavaLoginDto;
import com.studyjava.domain.vo.StudyJavaSysLoginVo;

public interface StudyJavaLoginService {

  StudyJavaSysLoginVo login(StudyJavaLoginDto studyJavaLogin);

  void register(com.studyjava.domain.dto.StudyJavaRegisterDto studyJavaRegisterDto);

  String getCaptcha() throws IOException;

  StudyJavaSysLoginVo refreshToken(String refreshToken);

  void logout();
}
