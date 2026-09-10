package com.studyjava.lab.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.studyjava.exception.StudyJavaException;
import com.studyjava.lab.config.JavaLabProperties;
import com.studyjava.lab.domain.JfrConfiguration;
import com.studyjava.lab.domain.JfrRecordingInfo;
import com.studyjava.lab.domain.StartJfrRecordingRequest;

class JfrRecordingServiceTest {

  @TempDir Path tempDirectory;

  private JfrRecordingService service;

  @BeforeEach
  void setUp() {
    JavaLabProperties properties = new JavaLabProperties();
    properties.getJfr().setDirectory(Path.of("recordings"));
    properties.getJfr().setMaxDurationSeconds(600);
    properties.getJfr().setMaxSizeMb(500);
    service = new JfrRecordingService(properties, tempDirectory);
  }

  @AfterEach
  void tearDown() {
    service.close();
  }

  @Test
  void startsStopsGeneratesFileAndRepeatedStopIsIdempotent() throws Exception {
    JfrRecordingInfo started =
        service.start(new StartJfrRecordingRequest(JfrConfiguration.DEFAULT, 30));
    JfrRecordingInfo stopped = service.stop(started.recordingId());
    JfrRecordingInfo stoppedAgain = service.stop(started.recordingId());
    Path file = service.getDownload(started.recordingId());

    assertEquals(JfrRecordingInfo.Status.RUNNING, started.status());
    assertEquals(JfrRecordingInfo.Status.STOPPED, stopped.status());
    assertEquals(stopped.stoppedAt(), stoppedAgain.stoppedAt());
    assertTrue(Files.isRegularFile(file));
    assertTrue(Files.size(file) > 0);
    service.delete(started.recordingId());
    assertFalse(Files.exists(file));
    assertThrows(StudyJavaException.class, () -> service.getDownload(started.recordingId()));
  }

  @Test
  void concurrentStartAllowsExactlyOneRecording() throws Exception {
    ExecutorService executor = Executors.newFixedThreadPool(2);
    Callable<Boolean> start =
        () -> {
          try {
            service.start(new StartJfrRecordingRequest(JfrConfiguration.PROFILE, 30));
            return true;
          } catch (StudyJavaException exception) {
            return false;
          }
        };
    try {
      List<Boolean> results =
          executor.invokeAll(List.of(start, start)).stream().map(this::get).toList();
      assertEquals(1, results.stream().filter(Boolean::booleanValue).count());
    } finally {
      executor.shutdownNow();
      assertTrue(executor.awaitTermination(2, TimeUnit.SECONDS));
    }
  }

  @Test
  void rejectsTraversalUnknownIdsAndDeletingRunningRecording() {
    JfrRecordingInfo started =
        service.start(new StartJfrRecordingRequest(JfrConfiguration.DEFAULT, 30));

    assertThrows(StudyJavaException.class, () -> service.getDownload("../../etc/passwd"));
    assertThrows(
        StudyJavaException.class,
        () -> service.getDownload("00000000-0000-0000-0000-000000000000"));
    assertThrows(StudyJavaException.class, () -> service.delete(started.recordingId()));
    assertThrows(StudyJavaException.class, () -> service.getDownload(started.recordingId()));
  }

  @Test
  void validatesRecordingDuration() {
    assertThrows(
        StudyJavaException.class,
        () -> service.start(new StartJfrRecordingRequest(JfrConfiguration.DEFAULT, 0)));
    assertThrows(
        StudyJavaException.class,
        () -> service.start(new StartJfrRecordingRequest(JfrConfiguration.DEFAULT, 601)));
  }

  @Test
  void closeStopsCurrentRecordingAndRejectsNewStarts() {
    JfrRecordingInfo started =
        service.start(new StartJfrRecordingRequest(JfrConfiguration.DEFAULT, 30));

    service.close();

    assertFalse(service.current().isPresent());
    assertEquals(JfrRecordingInfo.Status.STOPPED, service.stop(started.recordingId()).status());
    assertThrows(
        StudyJavaException.class,
        () -> service.start(new StartJfrRecordingRequest(JfrConfiguration.DEFAULT, 30)));
  }

  private boolean get(java.util.concurrent.Future<Boolean> future) {
    try {
      return future.get();
    } catch (Exception exception) {
      throw new AssertionError(exception);
    }
  }
}
