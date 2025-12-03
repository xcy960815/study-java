package com.studyjava.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class StudyJavaSysRoleVo {
    private Long id;
    private String roleName;
    private String roleCode;
    private Integer roleSort;
    private Integer status;
    private String remark;
    private String createBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
    private String updateBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
    private java.util.List<Long> menuIds;
    private java.util.List<String> menuNames;
}
