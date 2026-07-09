package com.studyjava.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PasswordUtilsTest {

  @Test
  void encodeProducesHashThatFitsPasswordColumn() {
    String hash = PasswordUtils.encode("123456");

    assertTrue(hash.length() <= 100);
    assertTrue(PasswordUtils.matches("123456", hash));
  }

  @Test
  void matchesLegacyMd5AndMarksItForUpgrade() {
    String legacyMd5 = "e10adc3949ba59abbe56e057f20f883e";

    assertTrue(PasswordUtils.matches("123456", legacyMd5));
    assertTrue(PasswordUtils.needsUpgrade(legacyMd5));
  }

  @Test
  void bcryptHashDoesNotNeedUpgrade() {
    String hash = PasswordUtils.encode("123456");

    assertFalse(PasswordUtils.needsUpgrade(hash));
  }
}
