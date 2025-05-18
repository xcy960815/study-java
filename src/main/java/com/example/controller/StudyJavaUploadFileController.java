package com.example.controller;

import java.io.IOException;
import com.example.domain.dto.StudyJavaUploadFileDto;
import com.example.domain.vo.StudyJavaUploadFileVo;
import com.example.service.StudyJavaUploadFileService;
import com.example.utils.ResponseGenerator;
import com.example.utils.ResponseResult;
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
     * @return 上传结果
     */
    @PostMapping("/upload")
    public ResponseResult<StudyJavaUploadFileVo> uploadFile(@RequestParam("file") MultipartFile file) {
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
            
            return ResponseGenerator.generateSuccessResult(fileVo);
        } catch (IOException e) {
            log.error("文件上传失败", e);
            StudyJavaUploadFileVo fileVo = new StudyJavaUploadFileVo();
            fileVo.setStatus("error");
            fileVo.setMessage("文件上传失败: " + e.getMessage());
            return ResponseGenerator.generateSuccessResult(fileVo);
        }
    }

    /**
     * 大文件分片上传
     * @param fileDto 上传文件DTO
     * @return 上传结果
     */
    @PostMapping("/upload/chunk")
    public ResponseResult<StudyJavaUploadFileVo> uploadLargeFile(@Valid StudyJavaUploadFileDto fileDto) {
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
            
            return ResponseGenerator.generateSuccessResult(fileVo);
        } catch (IOException e) {
            log.error("文件分片上传失败", e);
            StudyJavaUploadFileVo fileVo = new StudyJavaUploadFileVo();
            fileVo.setStatus("error");
            fileVo.setMessage("文件分片上传失败: " + e.getMessage());
            return ResponseGenerator.generateSuccessResult(fileVo);
        }
    }
    
    /**
     * 文件上传（兼容旧接口）
     * @param file MultipartFile
     * @return ResponseResult<String>
     * @deprecated 使用 /file/upload 替代
     */
    @Deprecated
    @PostMapping("/uploadFile")
    public ResponseResult<String> uploadFileOld(MultipartFile file) {
        try {
            return ResponseGenerator.generateSuccessResult(studyJavaUploadFileService.uploadFile(file));
        } catch (IOException e) {
            return ResponseGenerator.generateErrorResult(e.getMessage());
        }
    }

    /**
     * 大文件切片上传（兼容旧接口）
     * @param file MultipartFile
     * @return ResponseResult<String>
     * @deprecated 使用 /file/upload/chunk 替代
     */
    @Deprecated
    @PostMapping("/uploadLargeFile")
    public ResponseResult<String> uploadLargeFileOld(
            @RequestParam("file") MultipartFile file,
            @RequestParam("fileName") String fileName,
            @RequestParam("chunkIndex") int chunkIndex,
            @RequestParam("totalChunks") int totalChunks
    ) {
        try {
            return ResponseGenerator.generateSuccessResult(studyJavaUploadFileService.uploadLargeFile(file, fileName, chunkIndex, totalChunks));
        } catch (IOException e) {
            return ResponseGenerator.generateErrorResult("上传失败：" + e.getMessage());
        }
    }
}
