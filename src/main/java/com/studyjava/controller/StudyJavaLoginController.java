package com.studyjava.controller;

import java.io.IOException;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.studyjava.domain.dto.StudyJavaLoginDto;
import com.studyjava.domain.vo.StudyJavaSysLoginVo;
import com.studyjava.service.StudyJavaLoginService;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Validated
public class StudyJavaLoginController {
  @Resource private StudyJavaLoginService studyJavaLoginService;

  /**
   * 登录
   *
   * @param studyJavaLoginDto
   * @return StudyJavaSysLoginVo
   */
  @PostMapping("/login")
  public StudyJavaSysLoginVo login(@Valid @RequestBody StudyJavaLoginDto studyJavaLoginDto) {
    return studyJavaLoginService.login(studyJavaLoginDto);
  }

  /**
   * 获取验证码
   *
   * @return String
   */
  @GetMapping("/captcha")
  public String getCaptcha() throws IOException {
    return studyJavaLoginService.getCaptcha();
  }

  /**
   * 退出登录
   *
   * @return Boolean
   */
  @PostMapping("/logout")
  public Boolean logout() {
    studyJavaLoginService.logout();
    return true;
  }

  /**
   * 刷新token
   *
   * @param params
   * @return StudyJavaSysLoginVo
   */
  @PostMapping("/refreshToken")
  public StudyJavaSysLoginVo refreshToken(@RequestBody java.util.Map<String, String> params) {
    return studyJavaLoginService.refreshToken(params.get("refreshToken"));
  }
}
