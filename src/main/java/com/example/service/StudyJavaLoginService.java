package com.example.service;

import com.example.domain.dto.StudyJavaLoginDto;
import com.example.domain.vo.StudyJavaLoginVo;

public interface StudyJavaLoginService {

    public StudyJavaLoginDto login(StudyJavaLoginVo studyJavaLogin);

//    public void logout(StudyJavaLoginVo studyJavaLogin);
}
