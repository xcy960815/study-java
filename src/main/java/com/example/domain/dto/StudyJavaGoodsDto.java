package com.example.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @TableName study_java_goods_category
 */
@TableName(value ="study_java_goods_category")
@Data
public class StudyJavaGoodsDto implements Serializable {
    /**
     * 分类id
     */
    @TableId(type = IdType.AUTO)
    private Long categoryId;

    /**
     * 分类级别(1-一级分类 2-二级分类 3-三级分类)
     */
    private String categoryLevel;

    /**
     * 父分类id
     */
    private Long parentId;

    /**
     * 分类名称
     */
    private String categoryName;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;

    /**
     * 创建者id
     */
    private Integer createUser;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 修改者id
     */
    private Integer updateUser;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
