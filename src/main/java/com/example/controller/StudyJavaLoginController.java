package com.example.controller;


import com.example.domain.vo.StudyJavaLoginVo;
import com.example.service.StudyJavaLoginService;
import com.example.utils.ResponseGenerator;
import com.example.utils.ResponseResult;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@Controller
public class StudyJavaLoginController {
    @Resource
    private StudyJavaLoginService studyJavaLoginService;

    @PostMapping("/login")
    @ResponseBody
    public ResponseResult login(@RequestBody StudyJavaLoginVo studyJavaLoginDomain) {
        String token = studyJavaLoginService.login(studyJavaLoginDomain);

        Map<String,String> loginResult = new HashMap<>();
        loginResult.put("token",token);
        return ResponseGenerator.generatSuccessResult(loginResult);
    }
};
