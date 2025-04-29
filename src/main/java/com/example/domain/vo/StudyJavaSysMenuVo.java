package com.example.domain.vo;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 系统菜单实体
 */
@Data
@Entity
@Table(name = "study_java_sys_menu")
public class StudyJavaSysMenuVo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuId;

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

    /**
     * 创建时间
     */
    @CreationTimestamp
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @UpdateTimestamp
    private LocalDateTime updateTime;
} 