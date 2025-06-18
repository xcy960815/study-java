package com.example.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.domain.dao.StudyJavaSysRoleDao;
import com.example.domain.dto.StudyJavaSysRoleDto;
import com.example.domain.vo.StudyJavaSysRoleVo;

import java.util.List;

public interface StudyJavaSysRoleService extends IService<StudyJavaSysRoleDao> {

    /**
     * 分页查询角色列表
     */
    Page<StudyJavaSysRoleVo> getRoleList(Integer pageNum, Integer pageSize, StudyJavaSysRoleDto queryDto);

    /**
     * 查询所有角色列表（不分页）
     */
    List<StudyJavaSysRoleVo> getAllRoleList();

    /**
     * 新增角色
     */
    boolean insertRole(StudyJavaSysRoleDto roleDto);

    /**
     * 修改角色
     */
    boolean updateRole(StudyJavaSysRoleDto roleDto);

    /**
     * 删除角色
     */
    boolean deleteRole(Long id);

    /**
     * 修改角色状态
     */
    boolean updateRoleStatus(StudyJavaSysRoleDto roleDto);
}
