package com.studyjava.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 文件上传响应VO */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudyJavaUploadFileVo {
  /** 文件路径 */
  private String filePath;

  /** 上传状态 */
  private String status;

  /** 上传消息 */
  private String message;
}
