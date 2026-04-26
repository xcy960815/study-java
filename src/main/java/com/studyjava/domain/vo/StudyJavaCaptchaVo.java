package com.studyjava.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudyJavaCaptchaVo {
  private String captchaId;
  private String captchaImage;
}
