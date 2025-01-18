package com.example.domain.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName study_java_carousel
 */
@TableName(value ="study_java_carousel")
@Data
public class StudyJavaCarouselDao implements Serializable {
    /**
     * 首页轮播图主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer carouselId;

    /**
     * 轮播图
     */
    private String carouselUrl;

    /**
     * 点击后的跳转地址(默认不跳转)
     */
    private String redirectUrl;

    /**
     * 排序值(字段越大越靠前)
     */
    private Integer carouselRank;

    /**
     * 删除标识字段(0-未删除 1-已删除)
     */
    private Integer isDeleted;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建者id
     */
    private Integer createUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改者id
     */
    private Integer updateUser;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}