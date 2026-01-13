package com.studyjava.domain.vo;

import java.io.Serial;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class StudyJavaSysMenuVo extends BaseVo {
  /** 菜单ID */
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

  private List<StudyJavaSysMenuVo> children;

  @Serial private static final long serialVersionUID = 1L;
}
