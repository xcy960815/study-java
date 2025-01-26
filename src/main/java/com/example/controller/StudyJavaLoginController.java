package com.example.controller;

import com.example.domain.dto.StudyJavaLoginDto;
import com.example.domain.vo.StudyJavaLoginVo;
import com.example.service.StudyJavaLoginService;
import com.example.utils.ResponseGenerator;
import com.example.utils.ResponseResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;



@RestController
public class StudyJavaLoginController {
    @Resource
    private StudyJavaLoginService studyJavaLoginService;

    @PostMapping("/login")
    @ResponseBody
    public ResponseResult login(@RequestBody StudyJavaLoginVo studyJavaLoginParams) {
        StudyJavaLoginDto loginResult = studyJavaLoginService.login(studyJavaLoginParams);
        return ResponseGenerator.generateSuccessResult(loginResult);
    }

    @PostMapping("/logout")
    @ResponseBody
    public ResponseResult logout() {
        // studyJavaLoginService.logout(studyJavaLoginParams);
        return ResponseGenerator.generateSuccessResult(null);
    }
};
