package com.studyjava.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.studyjava.domain.dao.StudyJavaSysRoleDao;
import com.studyjava.domain.dto.StudyJavaSysRoleDto;
import com.studyjava.domain.vo.StudyJavaSysRoleVo;
import com.studyjava.domain.vo.StudyJavaSysUserVo;
import com.studyjava.exception.StudyJavaException;
import com.studyjava.mapper.StudyJavaSysRoleMapper;
import com.studyjava.service.StudyJavaSysRoleService;
import com.studyjava.service.StudyJavaSysUserService;

import jakarta.annotation.Resource;

@Service
public class StudyJavaSysRoleServiceImpl implements StudyJavaSysRoleService {

  @Resource private StudyJavaSysRoleMapper studyJavaSysRoleMapper;

  @Resource private StudyJavaSysUserService studyJavaSysUserService;

  @Override
  public Page<StudyJavaSysRoleVo> getRoleList(
      Integer pageNum, Integer pageSize, StudyJavaSysRoleDto queryDto) {
    Page<StudyJavaSysRoleDao> page = new Page<>(pageNum, pageSize);

    // 将 DTO 转换为 DAO
    StudyJavaSysRoleDao studyJavaSysRoleDao = new StudyJavaSysRoleDao();
    BeanUtils.copyProperties(queryDto, studyJavaSysRoleDao);

    // 调用 mapper 方法进行分页查询
    IPage<StudyJavaSysRoleDao> rolePage =
        studyJavaSysRoleMapper.getRoleList(page, studyJavaSysRoleDao);

    // 转换为 VO
    Page<StudyJavaSysRoleVo> voPage = new Page<>();
    BeanUtils.copyProperties(rolePage, voPage, "records");

    List<StudyJavaSysRoleVo> voList =
        rolePage.getRecords().stream()
            .map(
                role -> {
                  StudyJavaSysRoleVo vo = new StudyJavaSysRoleVo();
                  BeanUtils.copyProperties(role, vo);
                  return vo;
                })
            .collect(Collectors.toList());

    voPage.setRecords(voList);
    return voPage;
  }

  /**
   * 获取所有角色列表
   *
   * @return List<StudyJavaSysRoleVo>
   */
  @Override
  public List<StudyJavaSysRoleVo> getAllRoleList() {
    // 调用 mapper 方法进行查询
    List<StudyJavaSysRoleDao> list = studyJavaSysRoleMapper.getAllRoleList();
    // 转换为 VO
    return list.stream().map(this::convertToVo).collect(Collectors.toList());
  }

  /**
   * 添加角色
   *
   * @param studyJavaSysRoleDto StudyJavaSysRoleDto
   * @return boolean
   */
  @Override
  public boolean insertRole(StudyJavaSysRoleDto studyJavaSysRoleDto) {
    StudyJavaSysRoleDao studyJavaSysRoleDao = convertToDao(studyJavaSysRoleDto);
    // 检查 roleCode 是否已存在
    StudyJavaSysRoleVo studyJavaSysRoleResponseVo = getRoleInfo(studyJavaSysRoleDto);
    if (studyJavaSysRoleResponseVo != null) {
      throw new StudyJavaException("角色编码已存在");
    }
    StudyJavaSysUserVo studyJavaSysUserVo = studyJavaSysUserService.getUserInfo();
    studyJavaSysRoleDao.setCreateTime(LocalDateTime.now());
    studyJavaSysRoleDao.setUpdateTime(LocalDateTime.now());
    studyJavaSysRoleDao.setCreateBy(studyJavaSysUserVo.getLoginName());
    studyJavaSysRoleDao.setUpdateBy(studyJavaSysUserVo.getLoginName());
    studyJavaSysRoleDao.setIsDeleted(0);
    return studyJavaSysRoleMapper.insertRole(studyJavaSysRoleDao) > 0;
  }

  /**
   * 更新角色
   *
   * @param studyJavaSysRoleDto StudyJavaSysRoleDto
   * @return boolean
   */
  @Override
  @Transactional
  public boolean updateRole(StudyJavaSysRoleDto studyJavaSysRoleDto) {
    // 检查 roleCode 是否已存在
    //        StudyJavaSysRoleVo studyJavaSysRoleResponseVo = getRoleInfo(studyJavaSysRoleDto);
    //        if (studyJavaSysRoleResponseVo != null) {
    //            throw new StudyJavaException("角色编码已存在");
    //        }
    StudyJavaSysRoleDao studyJavaSysRoleDao = convertToDao(studyJavaSysRoleDto);
    StudyJavaSysUserVo studyJavaSysUserVo = studyJavaSysUserService.getUserInfo();
    studyJavaSysRoleDao.setUpdateTime(LocalDateTime.now());
    studyJavaSysRoleDao.setUpdateBy(studyJavaSysUserVo.getLoginName());
    // 1. 删除旧的角色-菜单关系
    studyJavaSysRoleMapper.deleteRoleMenusByRoleId(studyJavaSysRoleDto.getId());

    // 2. 批量插入新的角色-菜单关系
    if (studyJavaSysRoleDto.getMenuIds() != null && !studyJavaSysRoleDto.getMenuIds().isEmpty()) {
      studyJavaSysRoleMapper.insertRoleMenus(
          studyJavaSysRoleDto.getId(), studyJavaSysRoleDto.getMenuIds());
    }
    // 3. 更新角色主表
    return studyJavaSysRoleMapper.updateRole(studyJavaSysRoleDao) > 0;
  }

  @Override
  @Transactional
  public boolean deleteRole(StudyJavaSysRoleDto roleDto) {
    // 先删除角色-菜单关联
    studyJavaSysRoleMapper.deleteRoleMenusByRoleId(roleDto.getId());
    // 再删除用户-角色关联
    studyJavaSysRoleMapper.deleteUserRolesByRoleId(roleDto.getId());
    // 最后删除角色主表
    return studyJavaSysRoleMapper.deleteRole(roleDto.getId()) > 0;
  }

  @Override
  public boolean updateRoleStatus(StudyJavaSysRoleDto roleDto) {
    StudyJavaSysRoleDao role = convertToDao(roleDto);
    return studyJavaSysRoleMapper.updateRoleStatus(role) > 0;
  }

  @Override
  public StudyJavaSysRoleVo getRoleInfo(StudyJavaSysRoleDto studyJavaSysRoleDto) {
    StudyJavaSysRoleDao studyJavaSysRoleDao = convertToDao(studyJavaSysRoleDto);
    return convertToVo(studyJavaSysRoleMapper.getRoleInfo(studyJavaSysRoleDao));
  }

  /** 将 Dao 对象转换为 Vo 对象 */
  private StudyJavaSysRoleVo convertToVo(StudyJavaSysRoleDao dao) {
    if (dao == null) {
      return null;
    }
    StudyJavaSysRoleVo vo = new StudyJavaSysRoleVo();
    BeanUtils.copyProperties(dao, vo);
    return vo;
  }

  /** 将 Dto 对象转换为 Dao 对象 */
  private StudyJavaSysRoleDao convertToDao(StudyJavaSysRoleDto dto) {
    if (dto == null) {
      return null;
    }
    StudyJavaSysRoleDao dao = new StudyJavaSysRoleDao();
    BeanUtils.copyProperties(dto, dao);
    return dao;
  }
}
