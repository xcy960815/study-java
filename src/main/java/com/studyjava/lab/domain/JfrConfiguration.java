package com.studyjava.lab.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import com.studyjava.exception.StudyJavaException;

public enum JfrConfiguration {
  DEFAULT("default"),
  PROFILE("profile");

  private final String value;

  JfrConfiguration(String value) {
    this.value = value;
  }

  @JsonCreator
  public static JfrConfiguration fromValue(String value) {
    for (JfrConfiguration configuration : values()) {
      if (configuration.value.equalsIgnoreCase(value)) {
        return configuration;
      }
    }
    throw new StudyJavaException("JFR configuration 仅支持 default 或 profile");
  }

  @JsonValue
  public String value() {
    return value;
  }
}
