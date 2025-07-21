package com.studyjava.domain.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传请求DTO
 */
@Data
public class StudyJavaUploadFileDto {
    /**
     * 上传的文件
     */
    @NotNull(message = "文件不能为空")
    private MultipartFile file;

    /**
     * 文件名（用于分片上传）
     */
    private String fileName;

    /**
     * 分片索引（用于分片上传）
     */
    @Min(value = 0, message = "分片索引不能小于0")
    private Integer chunkIndex;

    /**
     * 总分片数（用于分片上传）
     */
    @Min(value = 1, message = "总分片数不能小于1")
    private Integer totalChunks;
}
