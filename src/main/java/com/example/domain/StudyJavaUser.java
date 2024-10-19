package com.example.domain;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableField;
/**
 * 
 * @TableName study_java_user
 */
@Data
public class StudyJavaUser implements Serializable {
    /**
     * 用户主键id
     */
    private Long userId;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户年龄 不是表里面的字段
     */
    @TableField(exist = false)
    private int age;

    /**
     * 登陆名称(默认为手机号)
     */
    private String loginName;

    /**
     * MD5加密后的密码
     */
    private String passwordMd5;

    /**
     * 个性签名
     */
    private String introduceSign;

    /**
     * 收货地址
     */
    private String address;

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

    /**
     * 注册时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}