package com.studyjava.lab.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.text.ParseException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import com.studyjava.exception.StudyJavaException;
import com.studyjava.lab.config.JavaLabProperties;
import com.studyjava.lab.domain.JfrConfiguration;
import com.studyjava.lab.domain.JfrRecordingInfo;
import com.studyjava.lab.domain.StartJfrRecordingRequest;

import jakarta.annotation.PreDestroy;
import jdk.jfr.Configuration;
import jdk.jfr.Recording;
import jdk.jfr.RecordingState;

@Service
@ConditionalOnExpression("${java-lab.enabled:true} and ${java-lab.jfr.enabled:true}")
public class JfrRecordingService {

  private static final int ABSOLUTE_MAX_DURATION_SECONDS = 600;
  private static final int ABSOLUTE_MAX_SIZE_MB = 500;
  private static final long BYTES_PER_MB = 1024L * 1024L;

  private final ReentrantLock lifecycleLock = new ReentrantLock();
  private final Map<String, ManagedRecording> recordings = new HashMap<>();
  private final Path runtimeDirectory;
  private final Path recordingDirectory;
  private final int maxDurationSeconds;
  private final long maxSizeBytes;
  private String currentRecordingId;
  private boolean shuttingDown;

  public JfrRecordingService(JavaLabProperties properties) {
    this(properties, Path.of(System.getProperty("user.dir")));
  }

  JfrRecordingService(JavaLabProperties properties, Path runtimeDirectory) {
    JavaLabProperties.Jfr jfr = properties.getJfr();
    if (jfr.getMaxDurationSeconds() < 1
        || jfr.getMaxDurationSeconds() > ABSOLUTE_MAX_DURATION_SECONDS) {
      throw new StudyJavaException("java-lab.jfr.max-duration-seconds 必须在 1 到 600 之间");
    }
    if (jfr.getMaxSizeMb() < 1 || jfr.getMaxSizeMb() > ABSOLUTE_MAX_SIZE_MB) {
      throw new StudyJavaException("java-lab.jfr.max-size-mb 必须在 1 到 500 之间");
    }
    Path root = runtimeDirectory.toAbsolutePath().normalize();
    this.runtimeDirectory = root;
    Path configured = jfr.getDirectory();
    this.recordingDirectory =
        (configured.isAbsolute() ? configured : root.resolve(configured))
            .toAbsolutePath()
            .normalize();
    if (!recordingDirectory.startsWith(root)) {
      throw new StudyJavaException("JFR 目录必须位于项目运行目录内");
    }
    this.maxDurationSeconds = jfr.getMaxDurationSeconds();
    this.maxSizeBytes = jfr.getMaxSizeMb() * BYTES_PER_MB;
  }

  public JfrRecordingInfo start(StartJfrRecordingRequest request) {
    validateRequest(request);
    lifecycleLock.lock();
    try {
      ensureNotShuttingDown();
      refreshCurrentRecording();
      if (currentRecordingId != null) {
        throw new StudyJavaException("同一时间最多只能有一个 JFR 录制任务");
      }
      Files.createDirectories(recordingDirectory);
      verifyDirectoryBoundary();
      String recordingId = UUID.randomUUID().toString();
      Path destination = registeredPath(recordingId);
      Recording recording = createRecording(request.configuration());
      try {
        recording.setName("study-java-" + recordingId);
        recording.setToDisk(true);
        recording.setDestination(destination);
        recording.setDuration(java.time.Duration.ofSeconds(request.durationSeconds()));
        recording.setMaxSize(maxSizeBytes);
        recording.start();
      } catch (RuntimeException | IOException exception) {
        recording.close();
        Files.deleteIfExists(destination);
        throw exception;
      }
      ManagedRecording managed =
          new ManagedRecording(
              recordingId,
              request.configuration(),
              request.durationSeconds(),
              destination,
              recording,
              Instant.now());
      recordings.put(recordingId, managed);
      currentRecordingId = recordingId;
      return managed.toInfo(maxSizeBytes);
    } catch (IOException | ParseException exception) {
      throw new StudyJavaException("无法启动 JFR 录制");
    } catch (NoClassDefFoundError | UnsupportedOperationException | SecurityException exception) {
      throw new StudyJavaException("当前 JVM 不支持 JFR");
    } finally {
      lifecycleLock.unlock();
    }
  }

  public Optional<JfrRecordingInfo> current() {
    lifecycleLock.lock();
    try {
      refreshCurrentRecording();
      if (currentRecordingId == null) {
        return Optional.empty();
      }
      return Optional.of(recordings.get(currentRecordingId).toInfo(maxSizeBytes));
    } finally {
      lifecycleLock.unlock();
    }
  }

  public JfrRecordingInfo stop(String recordingId) {
    lifecycleLock.lock();
    try {
      ManagedRecording managed = requireRecording(recordingId);
      refresh(managed);
      if (managed.stoppedAt != null) {
        return managed.toInfo(maxSizeBytes);
      }
      stopAndClose(managed);
      return managed.toInfo(maxSizeBytes);
    } finally {
      lifecycleLock.unlock();
    }
  }

  public Path getDownload(String recordingId) {
    lifecycleLock.lock();
    try {
      ManagedRecording managed = requireRecording(recordingId);
      refresh(managed);
      if (managed.stoppedAt == null) {
        throw new StudyJavaException("录制进行中，停止后才能下载");
      }
      Path path = safeRegisteredPath(managed);
      if (!Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS)) {
        throw new StudyJavaException("JFR 文件不存在");
      }
      return path;
    } finally {
      lifecycleLock.unlock();
    }
  }

  public void delete(String recordingId) {
    lifecycleLock.lock();
    try {
      ManagedRecording managed = requireRecording(recordingId);
      refresh(managed);
      if (managed.stoppedAt == null) {
        throw new StudyJavaException("录制进行中，不能删除");
      }
      Path path = safeRegisteredPath(managed);
      try {
        Files.deleteIfExists(path);
      } catch (IOException exception) {
        throw new StudyJavaException("删除 JFR 文件失败");
      }
      recordings.remove(recordingId);
      if (recordingId.equals(currentRecordingId)) {
        currentRecordingId = null;
      }
    } finally {
      lifecycleLock.unlock();
    }
  }

  @PreDestroy
  public void close() {
    lifecycleLock.lock();
    try {
      shuttingDown = true;
      for (ManagedRecording managed : recordings.values()) {
        try {
          refresh(managed);
          if (managed.stoppedAt == null) {
            stopAndClose(managed);
          }
        } catch (RuntimeException ignored) {
          managed.stoppedAt = Instant.now();
        } finally {
          if (managed.recording.getState() != RecordingState.CLOSED) {
            managed.recording.close();
          }
        }
      }
      currentRecordingId = null;
    } finally {
      lifecycleLock.unlock();
    }
  }

  private Recording createRecording(JfrConfiguration configuration)
      throws IOException, ParseException {
    return new Recording(Configuration.getConfiguration(configuration.value()));
  }

  private void validateRequest(StartJfrRecordingRequest request) {
    if (request == null || request.configuration() == null) {
      throw new StudyJavaException("configuration 不能为空");
    }
    if (request.durationSeconds() < 1
        || request.durationSeconds() > ABSOLUTE_MAX_DURATION_SECONDS
        || request.durationSeconds() > maxDurationSeconds) {
      throw new StudyJavaException(
          "durationSeconds 必须在 1 到 " + maxDurationSeconds + " 之间");
    }
  }

  private ManagedRecording requireRecording(String recordingId) {
    UUID parsed;
    try {
      parsed = UUID.fromString(recordingId);
    } catch (IllegalArgumentException | NullPointerException exception) {
      throw new StudyJavaException("recordingId 非法");
    }
    if (!parsed.toString().equals(recordingId)) {
      throw new StudyJavaException("recordingId 非法");
    }
    ManagedRecording managed = recordings.get(recordingId);
    if (managed == null) {
      throw new StudyJavaException("JFR 录制不存在");
    }
    return managed;
  }

  private Path registeredPath(String recordingId) {
    Path path = recordingDirectory.resolve(recordingId + ".jfr").normalize();
    if (!path.startsWith(recordingDirectory)) {
      throw new StudyJavaException("JFR 文件路径非法");
    }
    return path;
  }

  private Path safeRegisteredPath(ManagedRecording managed) {
    verifyDirectoryBoundary();
    Path expected = registeredPath(managed.recordingId);
    Path actual = managed.destination.toAbsolutePath().normalize();
    if (!actual.equals(expected)) {
      throw new StudyJavaException("JFR 文件路径非法");
    }
    return actual;
  }

  private void verifyDirectoryBoundary() {
    try {
      Path realRoot = runtimeDirectory.toRealPath();
      Path realDirectory = recordingDirectory.toRealPath();
      if (!realDirectory.startsWith(realRoot)) {
        throw new StudyJavaException("JFR 文件路径非法");
      }
    } catch (IOException exception) {
      throw new StudyJavaException("JFR 目录不可用");
    }
  }

  private void refreshCurrentRecording() {
    if (currentRecordingId != null) {
      refresh(recordings.get(currentRecordingId));
    }
  }

  private void refresh(ManagedRecording managed) {
    if (managed == null || managed.stoppedAt != null) {
      return;
    }
    RecordingState state = managed.recording.getState();
    if (state == RecordingState.STOPPED || state == RecordingState.CLOSED) {
      managed.stoppedAt = Instant.now();
      if (state != RecordingState.CLOSED) {
        managed.recording.close();
      }
      if (managed.recordingId.equals(currentRecordingId)) {
        currentRecordingId = null;
      }
    }
  }

  private void stopAndClose(ManagedRecording managed) {
    RecordingState state = managed.recording.getState();
    if (state == RecordingState.RUNNING) {
      managed.recording.stop();
    }
    if (managed.recording.getState() != RecordingState.CLOSED) {
      managed.recording.close();
    }
    managed.stoppedAt = Instant.now();
    if (managed.recordingId.equals(currentRecordingId)) {
      currentRecordingId = null;
    }
  }

  private void ensureNotShuttingDown() {
    if (shuttingDown) {
      throw new StudyJavaException("应用正在关闭，不能启动 JFR 录制");
    }
  }

  private static final class ManagedRecording {
    private final String recordingId;
    private final JfrConfiguration configuration;
    private final int durationSeconds;
    private final Path destination;
    private final Recording recording;
    private final Instant startedAt;
    private Instant stoppedAt;

    private ManagedRecording(
        String recordingId,
        JfrConfiguration configuration,
        int durationSeconds,
        Path destination,
        Recording recording,
        Instant startedAt) {
      this.recordingId = recordingId;
      this.configuration = configuration;
      this.durationSeconds = durationSeconds;
      this.destination = destination;
      this.recording = recording;
      this.startedAt = startedAt;
    }

    private JfrRecordingInfo toInfo(long maxSizeBytes) {
      long fileSize = 0;
      try {
        if (Files.isRegularFile(destination, LinkOption.NOFOLLOW_LINKS)) {
          fileSize = Files.size(destination);
        }
      } catch (IOException ignored) {
        // 文件状态会在下载时返回明确错误，状态查询不因此失败。
      }
      return new JfrRecordingInfo(
          recordingId,
          configuration,
          durationSeconds,
          maxSizeBytes,
          startedAt,
          stoppedAt,
          stoppedAt == null ? JfrRecordingInfo.Status.RUNNING : JfrRecordingInfo.Status.STOPPED,
          fileSize);
    }
  }
}
