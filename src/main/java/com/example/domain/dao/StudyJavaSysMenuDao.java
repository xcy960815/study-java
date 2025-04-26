package com.example.domain.dao;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统菜单表(StudyJavaSysMenuDao)表实体类
 *
 * @author makejava
 * @since 2025-04-25 18:27:54
 */
@Setter
@Getter
public class StudyJavaSysMenuDao {
    //菜单ID
    private Long id;
    //父级菜单ID，根菜单为 null
    private Long parentId;
    //菜单名称
    private String name;
    //菜单path，点击菜单时跳转的链接
    private String path;
    //菜单图标，使用字体图标或者图像路径
    private String icon;
    //菜单类型：1=菜单，2=按钮，3=其他
    private Integer type;
    //菜单排序，数值越小越靠前
    private Integer orderNum;
    //是否可见：1=可见，0=不可见
    private Integer visible;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //创建者
    private String createBy;
    //更新者
    private String updateBy;


}

