package com.studyjava.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StudyJavaRegisterDto {
  /** 用户名 */
  @NotBlank(message = "用户名不能为空")
  private String username;

  /** 密码 */
  @NotBlank(message = "密码不能为空")
  private String password;

  /** 确认密码 */
  @NotBlank(message = "确认密码不能为空")
  private String confirmPassword;

  /** 验证码 */
  @NotBlank(message = "验证码不能为空")
  private String captcha;
}
