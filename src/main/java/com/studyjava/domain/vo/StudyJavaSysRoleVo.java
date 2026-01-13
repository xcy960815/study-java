package com.studyjava.domain.vo;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class StudyJavaSysRoleVo extends BaseVo {
  private Long id;
  private String roleName;
  private String roleCode;
  private Integer roleSort;
  private Integer status;
  private List<Long> menuIds;
  private List<String> menuNames;
}
