package com.studyjava.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class AuthRedisKeysTest {

  @Test
  void accessTokenKeyContainsUserId() {
    assertEquals("study-java-token:1001", AuthRedisKeys.accessTokenKey("1001"));
  }

  @Test
  void refreshTokenKeyContainsUserId() {
    assertEquals("study-java-refresh-token:1001", AuthRedisKeys.refreshTokenKey("1001"));
  }

  @Test
  void captchaKeyContainsCaptchaId() {
    assertEquals("study-java-captcha:uuid-42", AuthRedisKeys.captchaKey("uuid-42"));
  }

  @Test
  void permissionsCacheKeyContainsUserId() {
    assertEquals("study-java-permissions:1001", AuthRedisKeys.permissionsCacheKey("1001"));
  }

  @Test
  void distinctUserIdsProduceDistinctKeys() {
    String keyForA = AuthRedisKeys.accessTokenKey("7");
    String keyForB = AuthRedisKeys.accessTokenKey("8");

    assertEquals(true, keyForA.equals("study-java-token:7"));
    assertEquals(true, keyForB.equals("study-java-token:8"));
  }
}
