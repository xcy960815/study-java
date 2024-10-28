package com.example.controller;


import com.example.domain.StudyJavaLoginDomain;
import com.example.service.StudyJavaLoginService;
import com.example.utils.ResponseGenerator;
import com.example.utils.ResponseResult;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
public class StudyJavaLoginController {
    @Resource
    private StudyJavaLoginService studyJavaLoginService;
    @PostMapping("/login")
    @ResponseBody
    public ResponseResult login(@RequestBody StudyJavaLoginDomain studyJavaLoginDomain) {
        String token = studyJavaLoginService.login(studyJavaLoginDomain);
        return ResponseGenerator.generatSuccessResult(token);
    }
};
