package com.studyjava.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.List;

/**
 * TableName study_java_sys_user
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StudyJavaSysUserVo extends BaseVo {
    /**
     * 用户主键id
     */
    private Long id;

    /**
     * 多角色支持
     */
    private List<Long> roleIds;
    private List<String> roleNames;
    private List<String> roleCodes;

    /**
     * 权限列表
     */
    private List<String> permissions;

    /**
     * 用户昵称
     */
    @NotBlank(message = "用户昵称不能为空")
    private String nickName;

    /**
     * 登陆名称(默认为手机号)
     */
    @NotBlank(message = "登陆名称不能为空")
    private String loginName;

    /**
     * MD5加密后的密码
     */
    private String passwordMd5;

    /**
     * 新密码 用于修改密码
     */
    private String newPasswordMd5;

    /**
     * 新密码的确认密码 用于修改密码
     */
    private String confirmNewPasswordMd5;

    /**
     * 个性签名
     */
    @NotBlank(message = "个性签名不能为空")
    private String introduceSign;

    /**
     * 收货地址
     */
    @NotBlank(message = "收货地址不能为空")
    private String address;

    /**
     * 锁定标识字段(0-未锁定 1-已锁定)
     */
    @JsonIgnore
    private Integer lockedFlag;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 年龄
     */
    private Integer age;

    @Serial
    private static final long serialVersionUID = 1L;
}
