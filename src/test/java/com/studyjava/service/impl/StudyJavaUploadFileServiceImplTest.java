package com.studyjava.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;

import com.studyjava.exception.StudyJavaException;

/**
 * 上传服务测试。
 *
 * <p>服务的上传目录是类加载时基于 user.dir 解析的 static final 字段，因此必须在类加载前
 * 把 user.dir 切到临时目录。static @TempDir 在所有测试方法前创建，满足时序。
 */
class StudyJavaUploadFileServiceImplTest {

  @TempDir static Path tempDir;

  private static String originalUserDir;
  private StudyJavaUploadFileServiceImpl service;

  @BeforeAll
  static void switchUserDir() {
    originalUserDir = System.getProperty("user.dir");
    System.setProperty("user.dir", tempDir.toString());
  }

  @AfterAll
  static void restoreUserDir() throws IOException {
    System.setProperty("user.dir", originalUserDir);
    try (Stream<Path> paths = Files.walk(tempDir)) {
      paths.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
    }
  }

  @BeforeEach
  void setUp() {
    service = new StudyJavaUploadFileServiceImpl();
  }

  private MockMultipartFile file(String name, byte[] content) {
    return new MockMultipartFile("file", name, "application/octet-stream", content);
  }

  private Path uploadFilesDir() {
    return tempDir.resolve("uploadFiles");
  }

  private Path uploadLargeFilesDir() {
    return tempDir.resolve("uploadLargeFiles");
  }

  @Test
  void uploadFileWritesFileAndReturnsPath() {
    String result = service.uploadFile(file("hello.txt", "hello world".getBytes()));

    Path expected = uploadFilesDir().resolve("hello.txt");
    assertEquals(
        expected.toAbsolutePath().normalize(), Path.of(result).toAbsolutePath().normalize());
    assertTrue(Files.isRegularFile(expected));
  }

  @Test
  void uploadFileRejectsEmptyFile() {
    assertThrows(
        StudyJavaException.class, () -> service.uploadFile(file("empty.txt", new byte[0])));
  }

  @Test
  void uploadFileRejectsNullFile() {
    assertThrows(StudyJavaException.class, () -> service.uploadFile(null));
  }

  @Test
  void uploadFileRejectsOversizedFile() {
    byte[] oversized = new byte[100 * 1024 * 1024 + 1];

    assertThrows(
        StudyJavaException.class, () -> service.uploadFile(file("big.bin", oversized)));
  }

  @Test
  void uploadFileStripsPathTraversalAndKeepsBaseName() {
    // getFileName() 先剥掉路径部分，剩余 evil.sh 本身已是合法文件名
    String result = service.uploadFile(file("../../evil.sh", "data".getBytes()));

    Path stored = Path.of(result).toAbsolutePath().normalize();
    assertTrue(
        stored.startsWith(uploadFilesDir().toAbsolutePath().normalize()),
        "文件必须落在 uploadFiles 内: " + result);
    assertEquals("evil.sh", stored.getFileName().toString());
  }

  @Test
  void uploadFileReplacesNonAsciiCharactersWithUnderscore() {
    service.uploadFile(file("我的文件.txt", "data".getBytes()));

    // 非 [A-Za-z0-9._-] 的字符一律替换成下划线，4 个汉字 -> 4 个下划线
    assertTrue(
        Files.isRegularFile(uploadFilesDir().resolve("____.txt")),
        "文件名应只包含 [A-Za-z0-9._-]");
  }

  @Test
  void uploadFileGeneratesFallbackNameWhenMissing() {
    String result = service.uploadFile(file(null, "data".getBytes()));

    Path stored = Path.of(result);
    assertTrue(Files.isRegularFile(stored), "应生成随机文件名: " + result);
    // UUID 只包含 [0-9a-f-]
    assertTrue(
        stored.getFileName().toString().matches("[0-9a-f-]+"),
        "回退名应为 UUID: " + stored.getFileName());
  }

  @Test
  void uploadLargeFileMergesChunksWhenComplete() {
    String firstResult =
        service.uploadLargeFile(file("large.bin", "chunk-0-".getBytes()), "large.bin", 0, 2);
    String secondResult =
        service.uploadLargeFile(file("large.bin", "chunk-1".getBytes()), "large.bin", 1, 2);

    assertTrue(firstResult.startsWith("分片 0"), firstResult);
    assertEquals("上传完成: large.bin", secondResult);
    Path merged = uploadLargeFilesDir().resolve("large.bin");
    assertTrue(Files.isRegularFile(merged));
    assertEquals("chunk-0-chunk-1", readAllBytes(merged));
    Path chunk0 = uploadLargeFilesDir().resolve("large.bin.part.0");
    assertFalse(Files.exists(chunk0), "合并后分片应被删除");
  }

  @Test
  void uploadLargeFileRejectsInvalidChunkIndex() {
    assertThrows(
        StudyJavaException.class,
        () -> service.uploadLargeFile(file("a.bin", new byte[1]), "a.bin", 5, 2));
    assertThrows(
        StudyJavaException.class,
        () -> service.uploadLargeFile(file("a.bin", new byte[1]), "a.bin", -1, 2));
  }

  @Test
  void uploadLargeFileRejectsBlankFileName() {
    assertThrows(
        StudyJavaException.class,
        () -> service.uploadLargeFile(file("a.bin", new byte[1]), "  ", 0, 2));
  }

  @Test
  void uploadLargeFileRejectsInvalidTotalChunks() {
    assertThrows(
        StudyJavaException.class,
        () -> service.uploadLargeFile(file("a.bin", new byte[1]), "a.bin", 0, 0));
    assertThrows(
        StudyJavaException.class,
        () -> service.uploadLargeFile(file("a.bin", new byte[1]), "a.bin", 0, 10_001));
  }

  @Test
  void uploadLargeFileRejectsOversizedChunk() {
    byte[] oversized = new byte[20 * 1024 * 1024 + 1];

    assertThrows(
        StudyJavaException.class,
        () -> service.uploadLargeFile(file("big.part", oversized), "big.bin", 0, 2));
  }

  private static byte[] readAllBytes(Path path) {
    try {
      return Files.readAllBytes(path);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }
}
