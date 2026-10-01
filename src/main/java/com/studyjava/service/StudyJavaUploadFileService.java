package com.studyjava.service;

import org.springframework.web.multipart.MultipartFile;

public interface StudyJavaUploadFileService {
  String uploadLargeFile(MultipartFile file, String fileName, int chunkIndex, int totalChunks);

  String uploadFile(MultipartFile file);
}
