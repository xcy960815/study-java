package com.studyjava.controller;

import com.studyjava.domain.dto.StudyJavaLoginDto;
import com.studyjava.domain.vo.StudyJavaSysLoginVo;
import com.studyjava.service.StudyJavaLoginService;
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
    public StudyJavaSysLoginVo login(@Valid @RequestBody StudyJavaLoginDto studyJavaLoginDto) {
        return studyJavaLoginService.login(studyJavaLoginDto);
    }

    @GetMapping("/captcha")
    public String getCaptcha() throws IOException {
        return studyJavaLoginService.getCaptcha();
    }

    @PostMapping("/logout")
    public void logout() {
        // studyJavaLoginService.logout(studyJavaLoginParams);
    }

}
