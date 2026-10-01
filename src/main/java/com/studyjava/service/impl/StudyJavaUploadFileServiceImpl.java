package com.studyjava.service.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.studyjava.exception.StudyJavaException;
import com.studyjava.service.StudyJavaUploadFileService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StudyJavaUploadFileServiceImpl implements StudyJavaUploadFileService {
  private static final long MAX_NORMAL_FILE_SIZE = 100L * 1024 * 1024;
  private static final long MAX_CHUNK_SIZE = 20L * 1024 * 1024;
  private static final int MAX_TOTAL_CHUNKS = 10_000;

  /** 项目根目录 */
  private static final Path BASE_PATH = Path.of(System.getProperty("user.dir")).toAbsolutePath();

  /** 大文件上传路径 */
  private static final String FOLDERS_LARGE_NAME = "uploadLargeFiles";

  /** 大文件存储路径 */
  private static final Path FOLDERS_LARGE_PATH = BASE_PATH.resolve(FOLDERS_LARGE_NAME).normalize();

  /** 记录已上传的分片 */
  private static final ConcurrentMap<String, Set<Integer>> uploadedChunks =
      new ConcurrentHashMap<>();

  /** 同一文件的分片写入和合并必须串行，避免两个请求同时合并出半截文件 */
  private static final ConcurrentMap<String, Object> uploadLocks = new ConcurrentHashMap<>();

  /** 常规文件上传路径 */
  private static final String FOLDERS_NAME = "uploadFiles";

  /** 常规文件存储路径 */
  private static final Path FOLDERS_PATH = BASE_PATH.resolve(FOLDERS_NAME).normalize();

  /**
   * 常规文件上传 上传到服务器上
   *
   * @param file MultipartFile
   * @return String
   */
  @Override
  public String uploadFile(MultipartFile file) {
    validateFile(file, MAX_NORMAL_FILE_SIZE);
    try {
      Files.createDirectories(FOLDERS_PATH);
      Path targetFilePath = resolveUploadPath(FOLDERS_PATH, file.getOriginalFilename());
      file.transferTo(targetFilePath);
      return targetFilePath.toString();
    } catch (IOException e) {
      log.error("文件上传失败", e);
      throw new StudyJavaException("文件上传失败");
    }
  }

  /**
   * 大文件上传
   *
   * @param file MultipartFile
   * @param fileName String
   * @param chunkIndex int
   * @param totalChunks int
   * @return String
   */
  @Override
  public String uploadLargeFile(
      MultipartFile file, String fileName, int chunkIndex, int totalChunks) {
    validateFile(file, MAX_CHUNK_SIZE);
    validateChunkParams(fileName, chunkIndex, totalChunks);
    String safeFileName = sanitizeFileName(fileName);
    String uploadKey = safeFileName + ":" + totalChunks;
    Object lock = uploadLocks.computeIfAbsent(uploadKey, key -> new Object());
    synchronized (lock) {
      try {
        Files.createDirectories(FOLDERS_LARGE_PATH);
        Path chunkPath =
            resolveUploadPath(FOLDERS_LARGE_PATH, safeFileName + ".part." + chunkIndex);
        file.transferTo(chunkPath);

        Set<Integer> chunks =
            uploadedChunks.computeIfAbsent(uploadKey, key -> ConcurrentHashMap.newKeySet());
        chunks.add(chunkIndex);

        if (chunks.size() == totalChunks) {
          mergeFile(safeFileName, totalChunks);
          uploadedChunks.remove(uploadKey);
          uploadLocks.remove(uploadKey, lock);
          return "上传完成: " + safeFileName;
        }
        return "分片 " + chunkIndex + " 上传成功";
      } catch (IOException e) {
        log.error("大文件分片上传失败", e);
        throw new StudyJavaException("上传失败");
      }
    }
  }

  /**
   * 合并分片文件。先写入临时文件再替换目标，避免中途失败时把半截内容追加到已有文件上。
   */
  private void mergeFile(String fileName, int totalChunks) {
    Path mergedPath = resolveUploadPath(FOLDERS_LARGE_PATH, fileName);
    Path tempPath = resolveUploadPath(FOLDERS_LARGE_PATH, fileName + ".merging");
    try {
      Files.deleteIfExists(tempPath);
      try (OutputStream outputStream = Files.newOutputStream(tempPath)) {
        for (int i = 0; i < totalChunks; i++) {
          Path chunkPath = resolveUploadPath(FOLDERS_LARGE_PATH, fileName + ".part." + i);
          if (!Files.isRegularFile(chunkPath)) {
            throw new StudyJavaException("分片缺失");
          }
          Files.copy(chunkPath, outputStream);
        }
      }
      Files.move(
          tempPath,
          mergedPath,
          StandardCopyOption.REPLACE_EXISTING,
          StandardCopyOption.ATOMIC_MOVE);
    } catch (StudyJavaException e) {
      deleteQuietly(tempPath);
      throw e;
    } catch (IOException e) {
      deleteQuietly(tempPath);
      log.error("合并分片文件失败", e);
      throw new StudyJavaException("合并分片文件失败");
    }

    for (int i = 0; i < totalChunks; i++) {
      deleteQuietly(resolveUploadPath(FOLDERS_LARGE_PATH, fileName + ".part." + i));
    }
  }

  private void deleteQuietly(Path path) {
    try {
      Files.deleteIfExists(path);
    } catch (IOException e) {
      log.warn("删除临时文件失败: {}", path, e);
    }
  }

  private void validateFile(MultipartFile file, long maxSize) {
    if (file == null || file.isEmpty()) {
      throw new StudyJavaException("上传的文件为空");
    }
    if (file.getSize() > maxSize) {
      throw new StudyJavaException("上传文件超过大小限制");
    }
  }

  private void validateChunkParams(String fileName, int chunkIndex, int totalChunks) {
    if (fileName == null || fileName.isBlank()) {
      throw new StudyJavaException("文件名不能为空");
    }
    if (totalChunks <= 0 || totalChunks > MAX_TOTAL_CHUNKS) {
      throw new StudyJavaException("分片总数不合法");
    }
    if (chunkIndex < 0 || chunkIndex >= totalChunks) {
      throw new StudyJavaException("分片序号不合法");
    }
  }

  private Path resolveUploadPath(Path baseDir, String originalFileName) {
    String safeFileName = sanitizeFileName(originalFileName);
    Path targetPath = baseDir.resolve(safeFileName).normalize();
    if (!targetPath.startsWith(baseDir)) {
      throw new StudyJavaException("文件名不合法");
    }
    return targetPath;
  }

  private String sanitizeFileName(String originalFileName) {
    String fallbackName = UUID.randomUUID().toString();
    if (originalFileName == null || originalFileName.isBlank()) {
      return fallbackName;
    }
    String fileName = Path.of(originalFileName).getFileName().toString();
    fileName = fileName.replaceAll("[^A-Za-z0-9._-]", "_");
    if (fileName.isBlank() || ".".equals(fileName) || "..".equals(fileName)) {
      return fallbackName;
    }
    return fileName;
  }
}
