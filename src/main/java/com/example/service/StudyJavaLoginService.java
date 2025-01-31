package com.example.service;

import com.example.domain.dto.StudyJavaLoginDto;
import com.example.domain.vo.StudyJavaLoginVo;

import java.io.IOException;

public interface StudyJavaLoginService {

    public StudyJavaLoginDto login(StudyJavaLoginVo studyJavaLogin);

    public String getCaptcha() throws IOException;

//    public void logout(StudyJavaLoginVo studyJavaLogin);
}
