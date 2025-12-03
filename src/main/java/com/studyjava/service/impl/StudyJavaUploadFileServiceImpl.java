package com.studyjava.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.ConcurrentHashMap;
import com.studyjava.service.StudyJavaUploadFileService;

@Slf4j
@Service
public class StudyJavaUploadFileServiceImpl implements StudyJavaUploadFileService {
    /**
     * 项目根目录
     */
    private static final String BASE_PATH = System.getProperty("user.dir");
    /**
     * 大文件上传路径
     */
    private static final String FOLDERS_LARGE_NAME = "uploadLargeFiles";
    /**
     * 大文件存储路径
     */
    private static final String FOLDERS_LARGE_PATH = BASE_PATH + File.separator + FOLDERS_LARGE_NAME;
    /**
     * 记录已上传的分片
     */
    private static final ConcurrentHashMap<String, Integer> uploadedChunks = new ConcurrentHashMap<>();
    /**
     * 常规文件上传路径
     */
    private static final String FOLDERS_NAME = "uploadFiles";

    /**
     * 常规文件存储路径
     */
    private static final String FOLDERS_PATH = BASE_PATH + File.separator + FOLDERS_NAME;

    /**
     * 常规文件上传 上传到服务器上
     * @param file MultipartFile
     * @return String
     */
    @Override
    public String uploadFile(MultipartFile file) {
        try {
            File folder = new File(FOLDERS_PATH);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            // 拼接文件路径
            String targetFilePath = FOLDERS_PATH + File.separator + file.getOriginalFilename();
            file.transferTo(new File(targetFilePath));
            return targetFilePath;
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    /**
     * 大文件上传
     * @param file MultipartFile
     * @param fileName String
     * @param chunkIndex int
     * @param totalChunks int
     * @return String
     */
    @Override
    public String uploadLargeFile(
            MultipartFile file,
            String fileName,
            int chunkIndex,
            int totalChunks
    ) {
        try {
            File largeFolder = new File(FOLDERS_LARGE_PATH);
            if (!largeFolder.exists()) {
                largeFolder.mkdirs();
            }
            // 保存分片到临时文件
            File chunkFile = new File(FOLDERS_LARGE_PATH, fileName + "_" + chunkIndex);
            file.transferTo(chunkFile);

            // 记录已上传分片
            uploadedChunks.put(fileName + "_" + chunkIndex, 1);

            // 检查是否所有分片都上传完成
            if (uploadedChunks.size() == totalChunks) {
                mergeFile(fileName, totalChunks);
                return "上传完成: " + fileName;
            }
            return "分片 " + chunkIndex + " 上传成功";

        } catch (IOException e) {
            return "上传失败：" + e.getMessage();
        }
    }
    /**
     * 合并分片文件
     */
    private void mergeFile(String fileName, int totalChunks) {
        File mergedFile = new File(FOLDERS_LARGE_PATH, fileName);
        try (FileOutputStream fos = new FileOutputStream(mergedFile, true);
             BufferedOutputStream mergingStream = new BufferedOutputStream(fos)) {
            for (int i = 0; i < totalChunks; i++) {
                File chunkFile = new File(FOLDERS_LARGE_PATH, fileName + "_" + i);
                Files.copy(chunkFile.toPath(), mergingStream);
                chunkFile.delete(); // 删除分片文件
            }
            uploadedChunks.clear(); // 清空已上传记录
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
