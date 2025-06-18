package com.example.domain.dto;

import lombok.Data;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.Size;

@Data
public class StudyJavaSysRoleDto  {

    private Long id;

    /**
     * 角色名称
     */
//    @NotBlank(message = "角色名称不能为空")
//    @Size(max = 30, message = "角色名称长度不能超过30个字符")
    private String roleName;

    /**
     * 角色编码
     */
//    @NotBlank(message = "角色编码不能为空")
//    @Size(max = 100, message = "角色编码长度不能超过100个字符")
    private String roleCode;

    /**
     * 显示顺序
     */
//    @NotNull(message = "显示顺序不能为空")
    private Integer roleSort;

    /**
     * 角色状态（0正常 1停用）
     */
//    @NotNull(message = "角色状态不能为空")
    private Integer status;

    /**
     * 菜单树选择项是否关联显示
     */
    private Integer menuCheckStrictly;

    /**
     * 备注
     */
//    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;

//    /**
//     * 当前页码
//     */
//    private Long pageNum = 1L;
//
//    /**
//     * 每页显示记录数
//     */
//    private Long pageSize = 10L;
}
