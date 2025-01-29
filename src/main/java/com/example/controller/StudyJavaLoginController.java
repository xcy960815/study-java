package com.example.controller;

import com.example.domain.dto.StudyJavaLoginDto;
import com.example.domain.vo.StudyJavaLoginVo;
import com.example.service.StudyJavaLoginService;
import com.example.utils.ResponseGenerator;
import com.example.utils.ResponseResult;
import jakarta.annotation.Nullable;
import jakarta.annotation.Resource;
//import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Slf4j
@RestController
public class StudyJavaLoginController {
    @Autowired
    private Producer kaptchaProducer;

    @Resource
    private StudyJavaLoginService studyJavaLoginService;

    @PostMapping("/login")
    @ResponseBody
    public ResponseResult<Object> login(@RequestBody StudyJavaLoginVo studyJavaLoginParams) {

        if(StringUtils.isBlank(studyJavaLoginParams.getUsername()) ) {
            return ResponseGenerator.generateErrorResult("用户名不能为空");
        }
        if(StringUtils.isBlank(studyJavaLoginParams.getPassword())){
            return ResponseGenerator.generateErrorResult("密码不能为空");
        }
        if(StringUtils.isBlank(studyJavaLoginParams.getCaptcha()) ){
            return ResponseGenerator.generateErrorResult("验证码不能为空");
        }

//        String sessionCaptcha = (String) session.getAttribute("captcha");
//        log.info("sessionCaptcha{}", sessionCaptcha);
//        if (!sessionCaptcha.equalsIgnoreCase(studyJavaLoginParams.getCaptcha())) {
//            return ResponseGenerator.generateErrorResult("验证码错误");
//        }

        StudyJavaLoginDto loginResult = studyJavaLoginService.login(studyJavaLoginParams);
        return ResponseGenerator.generateSuccessResult(loginResult);
    }

    @GetMapping("/captcha")
    public ResponseResult<String> getCaptcha(HttpServletRequest request) throws IOException {
        // 生成验证码文本
        String captchaText = kaptchaProducer.createText();
//        log.info("captchaText{}", captchaText);
//        HttpSession session = request.getSession(); // 获取 Session
//        // 存入 session
//        session.setAttribute("captcha", captchaText);
//        log.info("captcha{}",session.getAttribute("captcha"));
        // 使用验证码文本生成图像
        BufferedImage captchaImage = kaptchaProducer.createImage(captchaText);

        // 将验证码图像转换为 Base64
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(captchaImage, "jpg", byteArrayOutputStream);
        String base64Image = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
        log.info("base64Image{}", base64Image);
        // 返回 Base64 字符串
        return ResponseGenerator.generateSuccessResult("data:image/jpeg;base64," + base64Image);
    }

    @PostMapping("/logout")
    @ResponseBody
    public ResponseResult<Nullable> logout() {
        // studyJavaLoginService.logout(studyJavaLoginParams);
        return ResponseGenerator.generateSuccessResult(null);
    }

};
