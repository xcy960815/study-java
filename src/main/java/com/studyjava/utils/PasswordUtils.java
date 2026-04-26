package com.studyjava.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public final class PasswordUtils {

  private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
  private static final String BCRYPT_PREFIX = "$2";
  private static final String MD5_PATTERN = "^[a-fA-F0-9]{32}$";

  private PasswordUtils() {}

  public static String encode(String rawPassword) {
    return PASSWORD_ENCODER.encode(rawPassword);
  }

  public static boolean matches(String rawPassword, String storedPassword) {
    if (rawPassword == null || storedPassword == null || storedPassword.isBlank()) {
      return false;
    }
    if (isBcryptHash(storedPassword)) {
      return PASSWORD_ENCODER.matches(rawPassword, storedPassword);
    }
    if (storedPassword.matches(MD5_PATTERN)) {
      return storedPassword.equalsIgnoreCase(md5Hex(rawPassword));
    }
    return storedPassword.equals(rawPassword);
  }

  public static boolean needsUpgrade(String storedPassword) {
    return storedPassword != null && !storedPassword.isBlank() && !isBcryptHash(storedPassword);
  }

  private static boolean isBcryptHash(String storedPassword) {
    return storedPassword.startsWith(BCRYPT_PREFIX);
  }

  private static String md5Hex(String value) {
    try {
      MessageDigest messageDigest = MessageDigest.getInstance("MD5");
      byte[] digest = messageDigest.digest(value.getBytes(StandardCharsets.UTF_8));
      StringBuilder hex = new StringBuilder(digest.length * 2);
      for (byte item : digest) {
        hex.append(String.format("%02x", item));
      }
      return hex.toString();
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException("MD5 algorithm is unavailable", e);
    }
  }
}
