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

    // private final StudyJavaUserService studyJavaUserService;

    // 使用构造函数注入 UserService
//    public OrderStudyJavaUserService(StudyJavaUserService studyJavaUserService) {
////        this.userService = studyJavaUserService;
//    }

    public String login(StudyJavaLoginDomain studyJavaLoginDomain) {
        String name = studyJavaLoginDomain.getName();
        String password = studyJavaLoginDomain.getPassword();

        String token = jwtTokenUtil.generateToken(name);
        return token ;
    }
}
