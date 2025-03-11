package com.example.controller;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import com.example.utils.ResponseGenerator;
import com.example.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.nio.file.Files;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RestController
public class StudyJavaUploadFileController {
    private static final String UPLOAD_LARGE_DIR = "uploadLargeFiles"; // 存储路径
    private static final ConcurrentHashMap<String, Integer> uploadedChunks = new ConcurrentHashMap<>(); // 记录已上传的分片
    private static final String UPLOAD_DIR = "uploadFiles";
    // 使用绝对路径避免相对路径问题
    private static final String BASE_PATH = System.getProperty("user.dir");
    /**
     * 文件上传
     * @param file MultipartFile
     * @return ResponseResult<String>
     */
    @PostMapping("/uploadFile")
    public ResponseResult<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String dirPath = BASE_PATH + File.separator + UPLOAD_DIR;
            log.info("dirPath {}",dirPath);
            File dirFolder = new File(dirPath);
            if (!dirFolder.exists()) {
                dirFolder.mkdirs();
            }
            // 拼接文件路径
            String targetFilePath = dirPath + File.separator + file.getOriginalFilename();
            file.transferTo(new File(targetFilePath));
            return ResponseGenerator.generateSuccessResult(targetFilePath);
        } catch (IOException e) {
            return ResponseGenerator.generateErrorResult(e.getMessage());
        }
    }

    /**
     * 大文件切片上传
     * @param file MultipartFile
     * @return ResponseResult<String>
     */
    @PostMapping("/uploadLargeFile")
    public void uploadLargeFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("fileName") String fileName,
            @RequestParam("chunkIndex") int chunkIndex,
            @RequestParam("totalChunks") int totalChunks
    ) {
        try {
            File dir = new File(UPLOAD_LARGE_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            // 保存分片到临时文件
            File chunkFile = new File(UPLOAD_LARGE_DIR, fileName + "_" + chunkIndex);
            file.transferTo(chunkFile);

            // 记录已上传分片
            uploadedChunks.put(fileName + "_" + chunkIndex, 1);

            // 检查是否所有分片都上传完成
            if (uploadedChunks.size() == totalChunks) {
                mergeFile(fileName, totalChunks);
                 ResponseGenerator.generateSuccessResult("上传完成: " + fileName);
            }

             ResponseGenerator.generateSuccessResult("分片 " + chunkIndex + " 上传成功");
        } catch (IOException e) {
             ResponseGenerator.generateErrorResult("上传失败：" + e.getMessage());
        }
    }
    /**
     * 合并分片文件
     */
    private void mergeFile(String fileName, int totalChunks) throws IOException {
        File mergedFile = new File(UPLOAD_LARGE_DIR, fileName);
        try (FileOutputStream fos = new FileOutputStream(mergedFile, true);
             BufferedOutputStream mergingStream = new BufferedOutputStream(fos)) {
            for (int i = 0; i < totalChunks; i++) {
                File chunkFile = new File(UPLOAD_LARGE_DIR, fileName + "_" + i);
                Files.copy(chunkFile.toPath(), mergingStream);
                chunkFile.delete(); // 删除分片文件
            }
            uploadedChunks.clear(); // 清空已上传记录
        }
    }
}
