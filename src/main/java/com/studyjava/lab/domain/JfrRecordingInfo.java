package com.studyjava.lab.domain;

import java.time.Instant;

public record JfrRecordingInfo(
    String recordingId,
    JfrConfiguration configuration,
    int durationSeconds,
    long maxSizeBytes,
    Instant startedAt,
    Instant stoppedAt,
    Status status,
    long fileSizeBytes) {
  public enum Status {
    RUNNING,
    STOPPED
  }
}
