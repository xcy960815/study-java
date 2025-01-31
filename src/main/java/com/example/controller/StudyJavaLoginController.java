package com.example.controller;

import com.example.domain.dto.StudyJavaLoginDto;
import com.example.domain.vo.StudyJavaLoginVo;
import com.example.service.StudyJavaLoginService;
import com.example.utils.ResponseGenerator;
import com.example.utils.ResponseResult;
import jakarta.annotation.Nullable;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.imageio.ImageIO;

import com.example.utils.RedisUtil;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
public class StudyJavaLoginController {



    @Resource
    private StudyJavaLoginService studyJavaLoginService;

    @PostMapping("/login")
    public ResponseResult<Object> login(@Valid @RequestBody StudyJavaLoginVo studyJavaLoginParams) {

//        if (!RedisUtil.hasKey(CAPTCHA_KEY)) {
//            return ResponseGenerator.generateErrorResult("验证码不存在");
//        }
//
//        String captcha = (String) RedisUtil.get(CAPTCHA_KEY);
//        if (!captcha.equalsIgnoreCase(studyJavaLoginParams.getCaptcha())) {
//            return ResponseGenerator.generateErrorResult("验证码错误");
//        }

        StudyJavaLoginDto loginResult = studyJavaLoginService.login(studyJavaLoginParams);
        return ResponseGenerator.generateSuccessResult(loginResult);
    }

    @GetMapping("/captcha")
    public ResponseResult<String> getCaptcha() throws IOException {
        String base64Image = studyJavaLoginService.getCaptcha();
        return ResponseGenerator.generateSuccessResult("data:image/jpeg;base64," + base64Image);
    }

    @PostMapping("/logout")
    public ResponseResult<Nullable> logout() {
        // studyJavaLoginService.logout(studyJavaLoginParams);
        return ResponseGenerator.generateSuccessResult(null);
    }

};
