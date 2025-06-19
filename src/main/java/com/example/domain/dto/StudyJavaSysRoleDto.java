package com.example.domain.dto;

import lombok.Data;

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
     * 备注
     */
//    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;

    /**
     * 角色对应的菜单ID集合
     */
    private java.util.List<Long> menuIds;

}
