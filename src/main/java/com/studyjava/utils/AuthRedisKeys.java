package com.studyjava.utils;

public final class AuthRedisKeys {

  private static final String ACCESS_TOKEN_PREFIX = "study-java-token";
  private static final String REFRESH_TOKEN_PREFIX = "study-java-refresh-token";
  private static final String CAPTCHA_PREFIX = "study-java-captcha";

  private AuthRedisKeys() {}

  public static String accessTokenKey(String userId) {
    return ACCESS_TOKEN_PREFIX + ":" + userId;
  }

  public static String refreshTokenKey(String userId) {
    return REFRESH_TOKEN_PREFIX + ":" + userId;
  }

  public static String captchaKey(String captchaId) {
    return CAPTCHA_PREFIX + ":" + captchaId;
  }
}
