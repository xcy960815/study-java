package com.example.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 *
 * @TableName study_java_admin_user
 */
@TableName(value ="study_java_admin_user")
@Data
public class StudyJavaAdminUserDto implements Serializable {
    /**
     * 管理员id
     */
    @TableId(type = IdType.AUTO)
    private Integer adminUserId;

    /**
     * 管理员登陆名称
     */
    @NotBlank(message = "用户名不能为空")
    private String loginUserName;

    /**
     * 管理员登陆密码
     */
    @NotBlank(message = "密码不能为空")
    private String loginPassword;

    /**
     * 管理员显示昵称
     */
    @NotBlank(message = "昵称不能为空")
    private String nickName;

    /**
     * 是否锁定 0未锁定 1已锁定无法登陆
     */
    private Integer locked;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
