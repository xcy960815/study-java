package com.example.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.util.Date;
import java.io.Serializable;

@Data
public class StudyJavaSysUserDto implements Serializable {
    /**
     * 用户主键id
     */
    private Long id;

    /**
     * 角色ID
     */
    private Long roleId;

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
     * 年龄 随机生成的
     */
    private Integer age;

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
     * 注册时间，格式化为 "yyyy-MM-dd"
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;

    /**
     * 密码
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
     * 头像
     */
    private String avatar;

    /**
     * 注销标识字段(0-正常 1-已注销)
     */
    @JsonIgnore
    private Integer isDeleted;

    /**
     * 锁定标识字段(0-未锁定 1-已锁定)
     */
    @JsonIgnore
    private Integer lockedFlag;

    @Serial
    private static final long serialVersionUID = 1L;
}
