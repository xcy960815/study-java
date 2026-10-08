package com.studyjava.lab;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.studyjava.exception.StudyJavaException;
import com.studyjava.lab.config.JavaLabProperties;
import com.studyjava.lab.service.JfrRecordingService;

class JfrRecordingServiceTest {

  @TempDir Path tempDirectory;

  @Test
  void testConstructorWithValidProperties() {
    JavaLabProperties properties = new JavaLabProperties();
    properties.getJfr().setEnabled(true);
    properties.getJfr().setMaxDurationSeconds(60);
    properties.getJfr().setMaxSizeMb(100);
    JfrRecordingService service = new JfrRecordingService(properties, tempDirectory);
    try {
      assertNotNull(service);
    } finally {
      service.close();
    }
  }

  @Test
  void testConstructorWithInvalidDuration() {
    JavaLabProperties properties = new JavaLabProperties();
    properties.getJfr().setMaxDurationSeconds(-1); // Invalid duration

    assertThrows(
        StudyJavaException.class, () -> new JfrRecordingService(properties, tempDirectory));
  }
}
