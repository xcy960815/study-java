package com.studyjava.domain.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName study_java_goods
 */
@TableName(value ="study_java_goods")
@Data
public class StudyJavaGoodsDao implements Serializable {
    /**
     * 商品id
     */
    @TableId(type = IdType.AUTO)
    private Long goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品简介
     */
    private String goodsIntro;

    /**
     * 商品分类id
     */
    private Long goodsCategoryId;

    /**
     * 商品封面图片
     */
    private String goodsCoverImg;

    /**
     * 商品轮播图
     */
    private String goodsCarousel;

    /**
     * 商品详情内容
     */
    private String goodsDetailContent;

    /**
     * 商品原价
     */
    private Integer originalPrice;

    /**
     * 商品销售价格
     */
    private Integer sellingPrice;

    /**
     * 商品库存数量
     */
    private Integer stockNum;

    /**
     * 商品标签
     */
    private String tag;

    /**
     * 商品销售状态(0-下架 1-上架)
     */
    private Integer goodsSellStatus;

    /**
     * 创建者id
     */
    private Integer createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改者id
     */
    private Integer updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
