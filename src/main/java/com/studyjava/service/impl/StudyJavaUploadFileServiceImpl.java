package com.studyjava.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
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
    try {
      Files.createDirectories(FOLDERS_LARGE_PATH);
      String safeFileName = sanitizeFileName(fileName);
      String uploadKey = safeFileName + ":" + totalChunks;
      // 保存分片到临时文件
      File chunkFile =
          resolveUploadPath(FOLDERS_LARGE_PATH, safeFileName + ".part." + chunkIndex).toFile();
      file.transferTo(chunkFile);

      // 记录已上传分片
      Set<Integer> chunks = uploadedChunks.computeIfAbsent(uploadKey, key -> ConcurrentHashMap.newKeySet());
      chunks.add(chunkIndex);

      // 检查是否所有分片都上传完成
      if (chunks.size() == totalChunks) {
        mergeFile(safeFileName, totalChunks);
        uploadedChunks.remove(uploadKey);
        return "上传完成: " + safeFileName;
      }
      return "分片 " + chunkIndex + " 上传成功";

    } catch (IOException e) {
      log.error("大文件分片上传失败", e);
      throw new StudyJavaException("上传失败");
    }
  }

  /** 合并分片文件 */
  private void mergeFile(String fileName, int totalChunks) {
    File mergedFile = resolveUploadPath(FOLDERS_LARGE_PATH, fileName).toFile();
    try (FileOutputStream fos = new FileOutputStream(mergedFile, true);
        BufferedOutputStream mergingStream = new BufferedOutputStream(fos)) {
      for (int i = 0; i < totalChunks; i++) {
        File chunkFile = resolveUploadPath(FOLDERS_LARGE_PATH, fileName + ".part." + i).toFile();
        Files.copy(chunkFile.toPath(), mergingStream);
        Files.deleteIfExists(chunkFile.toPath());
      }
    } catch (IOException e) {
      log.error("合并分片文件失败", e);
      throw new StudyJavaException("合并分片文件失败");
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
