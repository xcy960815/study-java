package com.example.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serial;
import java.util.Date;
import java.io.Serializable;

@Data
public class StudyJavaUserDto implements Serializable {
    /**
     * 用户主键id
     */
    private Long userId;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 登陆名称(默认为手机号)
     */
    private String loginName;

    /**
     * 年龄 随机生成的
     */
    private Integer age;

    /**
     * 个性签名
     */
    private String introduceSign;

    /**
     * 收货地址
     */
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
     * 头像
     */
    private String avatar;

    @Serial
    private static final long serialVersionUID = 1L;
}
