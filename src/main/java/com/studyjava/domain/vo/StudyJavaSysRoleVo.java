package com.studyjava.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class StudyJavaSysRoleVo extends BaseVo {
    private Long id;
    private String roleName;
    private String roleCode;
    private Integer roleSort;
    private Integer status;
    private List<Long> menuIds;
    private List<String> menuNames;
}
