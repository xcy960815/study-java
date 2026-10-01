package com.studyjava.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.studyjava.domain.dto.StudyJavaUploadFileDto;
import com.studyjava.domain.vo.StudyJavaUploadFileVo;
import com.studyjava.service.StudyJavaUploadFileService;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

/** 文件上传控制器 */
@RestController
@RequestMapping("/file")
public class StudyJavaUploadFileController {

  @Resource private StudyJavaUploadFileService studyJavaUploadFileService;

  /**
   * 常规文件上传。失败时由业务异常交给全局处理器，不再用 HTTP 200 包一层错误状态。
   *
   * @param file 上传的文件
   * @return StudyJavaUploadFileVo
   */
  @PostMapping("/upload")
  public StudyJavaUploadFileVo uploadFile(@RequestParam("file") MultipartFile file) {
    StudyJavaUploadFileVo fileVo = new StudyJavaUploadFileVo();
    fileVo.setFilePath(studyJavaUploadFileService.uploadFile(file));
    fileVo.setStatus("success");
    fileVo.setMessage("文件上传成功");
    return fileVo;
  }

  /**
   * 大文件分片上传。
   *
   * @param fileDto 上传文件DTO
   * @return StudyJavaUploadFileVo
   */
  @PostMapping("/upload/chunk")
  public StudyJavaUploadFileVo uploadLargeFile(@Valid StudyJavaUploadFileDto fileDto) {
    String result =
        studyJavaUploadFileService.uploadLargeFile(
            fileDto.getFile(),
            fileDto.getFileName(),
            fileDto.getChunkIndex(),
            fileDto.getTotalChunks());

    StudyJavaUploadFileVo fileVo = new StudyJavaUploadFileVo();
    if (result.startsWith("上传完成")) {
      fileVo.setFilePath(result.substring(result.indexOf(':') + 1).trim());
      fileVo.setStatus("completed");
      fileVo.setMessage("文件上传完成");
      return fileVo;
    }

    fileVo.setStatus("uploading");
    fileVo.setMessage(result);
    return fileVo;
  }

  /**
   * 文件上传（兼容旧接口）
   *
   * @param file MultipartFile
   * @return String
   * @deprecated 使用 /file/upload 替代
   */
  @Deprecated
  @PostMapping("/uploadFile")
  public String uploadFileOld(MultipartFile file) {
    return studyJavaUploadFileService.uploadFile(file);
  }

  /**
   * 大文件切片上传（兼容旧接口）
   *
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
      @RequestParam("totalChunks") int totalChunks) {
    return studyJavaUploadFileService.uploadLargeFile(file, fileName, chunkIndex, totalChunks);
  }
}
