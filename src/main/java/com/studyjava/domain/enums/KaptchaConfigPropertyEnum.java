package com.studyjava.domain.enums;

import lombok.Getter;

@Getter
public enum KaptchaConfigPropertyEnum {
  //    照片宽度
  IMAGE_WIDTH("kaptcha.image.width", "200"),
  //    图片高度
  IMAGE_HEIGHT("kaptcha.image.height", "50"),
  //    字体大小
  FONT_SIZE("kaptcha.textproducer.font.size", "40"),
  //    字符长度
  CHAR_LENGTH("kaptcha.textproducer.char.length", "4"),
  //    字体颜色
  FONT_COLOR("kaptcha.textproducer.font.color", "blue"),
  //    背景清理
  BACKGROUND_FROM("kaptcha.background.clear.from", "white"),
  //    背景清理
  BACKGROUND_TO("kaptcha.background.clear.to", "white");

  private final String key;

  private final String value;

  KaptchaConfigPropertyEnum(String key, String value) {
    this.key = key;
    this.value = value;
  }
}
