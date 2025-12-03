package com.studyjava.service;


import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;



public interface StudyJavaUploadFileService {
     String uploadLargeFile(MultipartFile file, String fileName, int chunkIndex, int totalChunks) throws IOException;
     String uploadFile(MultipartFile file) throws IOException;
}
