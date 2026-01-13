package com.studyjava.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class StudyJavaSysLoginVo extends StudyJavaSysUserVo {
  private String token;
  private String refreshToken;
}
