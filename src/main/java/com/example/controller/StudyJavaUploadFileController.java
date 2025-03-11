package com.example.controller;


import java.io.IOException;
import com.example.utils.ResponseGenerator;
import com.example.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;

@Slf4j
@RestController
public class StudyJavaUploadFileController {

    @PostMapping("/uploadFile")
    public ResponseResult<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String uploadPath = "uploadFile";
            // 使用绝对路径避免相对路径问题
            String basePath = System.getProperty("user.dir");
            log.info("basePath {}",basePath);
            String dirPath = basePath + File.separator + uploadPath;
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
}
