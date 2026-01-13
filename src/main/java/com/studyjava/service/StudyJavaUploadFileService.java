package com.studyjava.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface StudyJavaUploadFileService {
  String uploadLargeFile(MultipartFile file, String fileName, int chunkIndex, int totalChunks)
      throws IOException;

  String uploadFile(MultipartFile file) throws IOException;
}
