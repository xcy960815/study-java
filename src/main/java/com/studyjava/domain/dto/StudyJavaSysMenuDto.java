package com.studyjava.domain.dto;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/** 系统菜单表 @TableName study_java_sys_menu */
@TableName(value = "study_java_sys_menu")
@Data
public class StudyJavaSysMenuDto {
  /** 菜单ID */
  @TableId(type = IdType.AUTO)
  private Long id;

  /** 父菜单ID */
  private Long parentId;

  /** 菜单名称 */
  private String menuName;

  /** 菜单路径 */
  private String path;

  /** 组件路径 */
  private String component;

  /** 菜单图标 */
  private String icon;

  /** 菜单类型（0目录 1菜单 2按钮） */
  private Integer menuType;

  /** 权限标识 */
  private String perms;

  /** 排序 */
  private Integer orderNum;

  /** 创建时间 */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date createTime;

  /** 更新时间 */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date updateTime;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
