package com.example.domain.dto;

import lombok.Data;

@Data
public class StudyJavaSysRoleDto  {

    private Long id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 显示顺序
     */
    private Integer roleSort;

    /**
     * 角色状态（1正常 0停用）
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 角色对应的菜单ID集合
     */
    private java.util.List<Long> menuIds;

}
