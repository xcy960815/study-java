package com.example.dto;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;

@Data
public class StudyJavaUserDTO implements Serializable {
    /**
     * 用户主键id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 年龄
     */
    private int age;

    /**
     * 登陆名称(默认为手机号)
     */
    private String loginName;

    /**
     * 个性签名
     */
    private String introduceSign;

    /**
     * 收货地址
     */
    private String address;
    /**
     * 注册时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}
