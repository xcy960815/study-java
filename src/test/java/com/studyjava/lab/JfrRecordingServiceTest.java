package com.studyjava.lab;

import static org.junit.jupiter.api.Assertions.*;

import com.studyjava.lab.config.JavaLabProperties;
import com.studyjava.lab.service.JfrRecordingService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/**
 * Test to verify that JfrRecordingService can be instantiated correctly.
 * This test ensures Spring can create the bean without "No default constructor found" error.
 */
class JfrRecordingServiceTest {

  @Test
  void testConstructorWithValidProperties() throws IOException {
    // Given
    JavaLabProperties properties = new JavaLabProperties();
    properties.getJfr().setEnabled(true);
    properties.getJfr().setMaxDurationSeconds(60);
    properties.getJfr().setMaxSizeMb(100);
    Path tempDir = Files.createTempDirectory("jfr-test");

    try {
      // When - should not throw exception
      JfrRecordingService service = new JfrRecordingService(properties, tempDir);

      // Then - if we get here, the constructor worked
      assertNotNull(service);
    } finally {
      Files.walk(tempDir)
          .sorted(java.util.Comparator.reverseOrder())
          .forEach(path -> {
            try {
              Files.deleteIfExists(path);
            } catch (Exception ignored) {
              // ignore deletion errors
            }
          });
    }
  }

  @Test
  void testConstructorWithInvalidDuration() {
    // Given
    JavaLabProperties properties = new JavaLabProperties();
    properties.getJfr().setMaxDurationSeconds(-1); // Invalid duration

    // When/Then
    assertThrows(IllegalArgumentException.class, () -> {
      new JfrRecordingService(properties, Path.of(System.getProperty("user.dir")));
    });
  }
}
