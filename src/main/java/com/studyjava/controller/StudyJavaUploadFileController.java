package com.studyjava.controller;

import java.io.IOException;
import com.studyjava.domain.dto.StudyJavaUploadFileDto;
import com.studyjava.domain.vo.StudyJavaUploadFileVo;
import com.studyjava.service.StudyJavaUploadFileService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传控制器
 */
@Slf4j
@RestController
@RequestMapping("/file")
public class StudyJavaUploadFileController {

    @Resource
    private StudyJavaUploadFileService studyJavaUploadFileService;

    /**
     * 常规文件上传
     * @param file 上传的文件
     * @return StudyJavaUploadFileVo
     */
    @PostMapping("/upload")
    public StudyJavaUploadFileVo uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // 创建DTO
            StudyJavaUploadFileDto fileDto = new StudyJavaUploadFileDto();
            fileDto.setFile(file);

            // 调用服务
            String filePath = studyJavaUploadFileService.uploadFile(file);

            // 创建返回VO
            StudyJavaUploadFileVo fileVo = new StudyJavaUploadFileVo();
            fileVo.setFilePath(filePath);
            fileVo.setStatus("success");
            fileVo.setMessage("文件上传成功");

            return fileVo;
        } catch (IOException e) {
            log.error("文件上传失败", e);
            StudyJavaUploadFileVo fileVo = new StudyJavaUploadFileVo();
            fileVo.setStatus("error");
            fileVo.setMessage("文件上传失败: " + e.getMessage());
            return fileVo;
        }
    }

    /**
     * 大文件分片上传
     * @param fileDto 上传文件DTO
     * @return StudyJavaUploadFileVo
     */
    @PostMapping("/upload/chunk")
    public StudyJavaUploadFileVo uploadLargeFile(@Valid StudyJavaUploadFileDto fileDto) {
        try {
            // 调用服务
            String result = studyJavaUploadFileService.uploadLargeFile(
                    fileDto.getFile(),
                    fileDto.getFileName(),
                    fileDto.getChunkIndex(),
                    fileDto.getTotalChunks()
            );

            // 创建返回VO
            StudyJavaUploadFileVo fileVo = new StudyJavaUploadFileVo();

            // 判断是否上传完成
            if (result.startsWith("上传完成")) {
                fileVo.setFilePath(result.substring(result.indexOf(":") + 1).trim());
                fileVo.setStatus("completed");
                fileVo.setMessage("文件上传完成");
            } else if (result.startsWith("分片")) {
                fileVo.setStatus("uploading");
                fileVo.setMessage(result);
            } else {
                fileVo.setStatus("error");
                fileVo.setMessage(result);
            }

            return fileVo;
        } catch (IOException e) {
            log.error("文件分片上传失败", e);
            StudyJavaUploadFileVo fileVo = new StudyJavaUploadFileVo();
            fileVo.setStatus("error");
            fileVo.setMessage("文件分片上传失败: " + e.getMessage());
            return fileVo;
        }
    }

    /**
     * 文件上传（兼容旧接口）
     * @param file MultipartFile
     * @return String
     * @deprecated 使用 /file/upload 替代
     */
    @Deprecated
    @PostMapping("/uploadFile")
    public String uploadFileOld(MultipartFile file) {
        try {
            return studyJavaUploadFileService.uploadFile(file);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 大文件切片上传（兼容旧接口）
     * @param file MultipartFile
     * @return String
     * @deprecated 使用 /file/upload/chunk 替代
     */
    @Deprecated
    @PostMapping("/uploadLargeFile")
    public String uploadLargeFileOld(
            @RequestParam("file") MultipartFile file,
            @RequestParam("fileName") String fileName,
            @RequestParam("chunkIndex") int chunkIndex,
            @RequestParam("totalChunks") int totalChunks
    ) {
        try {
            return studyJavaUploadFileService.uploadLargeFile(file, fileName, chunkIndex, totalChunks);
        } catch (IOException e) {
            throw new RuntimeException("上传失败：" + e.getMessage());
        }
    }
}
