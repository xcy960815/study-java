package com.example.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @TableName study_java_goods
 */
@TableName(value ="study_java_goods")
@Data
public class StudyJavaGoodsVo implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long goodsId;
    private String goodsName;
    private String goodsIntro;
    private Long goodsCategoryId;
    private String goodsCoverImg;
    private String goodsCarousel;
    private String goodsDetailContent;
    private Integer originalPrice;
    private Integer sellingPrice;
    private Integer stockNum;
    private String tag;
    private Integer goodsSellStatus;
    private Integer createUser;
    private Date createTime;
    private Integer updateUser;
    private Date updateTime;
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
