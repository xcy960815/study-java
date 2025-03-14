package com.example.controller;

import java.io.IOException;
import com.example.service.StudyJavaUploadFileService;
import com.example.utils.ResponseGenerator;
import com.example.utils.ResponseResult;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
public class StudyJavaUploadFileController {

    @Resource
    private StudyJavaUploadFileService studyJavaUploadFileService;
    /**
     * 文件上传
     * @param file MultipartFile
     * @return ResponseResult<String>
     */
    @PostMapping("/uploadFile")
    public ResponseResult<String> uploadFile(MultipartFile file) {
        try {
            return ResponseGenerator.generateSuccessResult(studyJavaUploadFileService.uploadFile(file)) ;
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
    public ResponseResult<String> uploadLargeFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("fileName") String fileName,
            @RequestParam("chunkIndex") int chunkIndex,
            @RequestParam("totalChunks") int totalChunks
    ) {
        try {
            return ResponseGenerator.generateSuccessResult(studyJavaUploadFileService.uploadLargeFile(file, fileName, chunkIndex, totalChunks)) ;
        }catch (IOException e) {
            return ResponseGenerator.generateErrorResult("上传失败：" + e.getMessage());
        }
    }

}
