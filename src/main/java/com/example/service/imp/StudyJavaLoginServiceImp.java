package com.example.service.imp;


import com.example.domain.StudyJavaLoginDomain;
import com.example.service.StudyJavaLoginService;
import com.example.service.StudyJavaUserService;
import com.example.utils.JwtTokenUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class StudyJavaLoginServiceImp implements StudyJavaLoginService {
    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    private StudyJavaUserService studyJavaUserService; // 注入接口

    public String login(StudyJavaLoginDomain studyJavaLoginDomain) {
        String name = studyJavaLoginDomain.getName();
        String password = studyJavaLoginDomain.getPassword();

//        if (!studyJavaUserService.login(name, password)) {
//            return null;
//        }
        return JwtTokenUtil.generateToken(name);
    }
}
