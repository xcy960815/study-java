package com.example.service.imp;


import com.example.domain.StudyJavaLoginDomain;
import com.example.service.StudyJavaLoginService;
import com.example.utils.JwtTokenUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class StudyJavaLoginServiceImp implements StudyJavaLoginService {
    @Resource
   private JwtTokenUtil jwtTokenUtil;
    public String login(StudyJavaLoginDomain studyJavaLoginDomain) {
        String name = studyJavaLoginDomain.getName();
        String token = jwtTokenUtil.generateToken(name);
        return token ;
    }
}
