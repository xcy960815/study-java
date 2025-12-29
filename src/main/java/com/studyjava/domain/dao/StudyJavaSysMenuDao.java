package com.studyjava.domain.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统菜单表
 * @TableName study_java_sys_menu
 */
@TableName(value ="study_java_sys_menu")
@Data
@EqualsAndHashCode(callSuper = true)
public class StudyJavaSysMenuDao extends BaseDao {
    /**
     * 菜单ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 父菜单ID
     */
    private Long parentId;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 菜单路径
     */
    private String path;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 菜单类型（0目录 1菜单 2按钮）
     */
    private Integer menuType;

    /**
     * 权限标识
     */
    private String perms;

    /**
     * 排序
     */
    private Integer orderNum;

    @Serial
    private static final long serialVersionUID = 1L;
}
