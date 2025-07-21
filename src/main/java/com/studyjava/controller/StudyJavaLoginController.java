package com.studyjava.controller;

import com.studyjava.domain.dto.StudyJavaLoginDto;
import com.studyjava.domain.vo.StudyJavaSysLoginVo;
import com.studyjava.service.StudyJavaLoginService;
import com.studyjava.utils.ResponseGenerator;
import com.studyjava.utils.ResponseResult;
import jakarta.annotation.Nullable;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;

@Slf4j
@RestController
@Validated
public class StudyJavaLoginController {
    @Resource
    private StudyJavaLoginService studyJavaLoginService;

    @PostMapping("/login")
    public ResponseResult<StudyJavaSysLoginVo> login(@Valid @RequestBody StudyJavaLoginDto studyJavaLoginDto) {
        StudyJavaSysLoginVo loginResult = studyJavaLoginService.login(studyJavaLoginDto);
        return ResponseGenerator.generateSuccessResult(loginResult);
    }

    @GetMapping("/captcha")
    public ResponseResult<String> getCaptcha() throws IOException {
        String base64Image = studyJavaLoginService.getCaptcha();
        return ResponseGenerator.generateSuccessResult(base64Image);
    }

    @PostMapping("/logout")
    public ResponseResult<Nullable> logout() {
        // studyJavaLoginService.logout(studyJavaLoginParams);
        return ResponseGenerator.generateSuccessResult(null);
    }

}
