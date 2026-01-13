package com.studyjava.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.studyjava.domain.dto.StudyJavaSysRoleDto;
import com.studyjava.domain.vo.StudyJavaSysRoleVo;

public interface StudyJavaSysRoleService {

  /** 分页查询角色列表 */
  Page<StudyJavaSysRoleVo> getRoleList(
      Integer pageNum, Integer pageSize, StudyJavaSysRoleDto queryDto);

  /** 查询所有角色列表（不分页） */
  List<StudyJavaSysRoleVo> getAllRoleList();

  /** 新增角色 */
  boolean insertRole(StudyJavaSysRoleDto roleDto);

  /** 修改角色 */
  boolean updateRole(StudyJavaSysRoleDto roleDto);

  /** 删除角色 */
  boolean deleteRole(StudyJavaSysRoleDto roleDto);

  /** 修改角色状态 */
  boolean updateRoleStatus(StudyJavaSysRoleDto roleDto);

  /** 根据ID获取角色 */
  StudyJavaSysRoleVo getRoleInfo(StudyJavaSysRoleDto studyJavaSysRoleDto);
}
