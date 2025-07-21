package com.studyjava.domain.dao;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * TableName  study_java_sys_user
 */
@Data
public class StudyJavaSysUserDao implements Serializable {
    /**
     * 用户主键id
     */
    @TableId(value = "id", type = IdType.AUTO) // 指定主键
    private Long id;

    /**
     * 用户昵称
     */
    private String nickName;

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


    private Date createTime;

    /**
     * 头像
     */
    private String avatar;

    private List<Long> roleIds;

    private List<String> roleNames;

    private  List<String> roleCodes;

    @Serial
    private static final long serialVersionUID = 1L;
}
