package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.domain.dao.StudyJavaSysRoleDao;
import com.example.domain.dto.StudyJavaSysRoleDto;
import com.example.domain.vo.StudyJavaSysRoleVo;
import com.example.exception.StudyJavaException;
import com.example.mapper.StudyJavaSysRoleMapper;
import com.example.service.StudyJavaSysRoleService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudyJavaSysRoleServiceImpl implements StudyJavaSysRoleService {

    @Resource
    private StudyJavaSysRoleMapper studyJavaSysRoleMapper;

    @Override
    public Page<StudyJavaSysRoleVo> getRoleList(Integer pageNum, Integer pageSize, StudyJavaSysRoleDto queryDto) {
        Page<StudyJavaSysRoleDao> page = new Page<>(pageNum, pageSize);

        // 将 DTO 转换为 DAO
        StudyJavaSysRoleDao studyJavaSysRoleDao = new StudyJavaSysRoleDao();
        BeanUtils.copyProperties(queryDto, studyJavaSysRoleDao);

        // 调用 mapper 方法进行分页查询
        IPage<StudyJavaSysRoleDao> rolePage = studyJavaSysRoleMapper.getRoleList(page, studyJavaSysRoleDao);

        // 转换为 VO
        Page<StudyJavaSysRoleVo> voPage = new Page<>();
        BeanUtils.copyProperties(rolePage, voPage, "records");

        List<StudyJavaSysRoleVo> voList = rolePage.getRecords().stream()
                .map(role -> {
                    StudyJavaSysRoleVo vo = new StudyJavaSysRoleVo();
                    BeanUtils.copyProperties(role, vo);
                    return vo;
                })
                .collect(Collectors.toList());

        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public List<StudyJavaSysRoleVo> getAllRoleList() {
        // 调用 mapper 方法进行查询
        List<StudyJavaSysRoleDao> list = studyJavaSysRoleMapper.getAllRoleList();
        // 转换为 VO
        return list.stream().map(this::convertToVo).collect(Collectors.toList());
    }

    @Override
    public boolean insertRole(StudyJavaSysRoleDto roleDto) {
        // 检查 roleCode 是否已存在
        StudyJavaSysRoleDao  studyJavaSysRoleDao = new StudyJavaSysRoleDao();
        studyJavaSysRoleDao.setRoleCode(roleDto.getRoleCode());
        StudyJavaSysRoleDao studyJavaSysRoleResultDao = studyJavaSysRoleMapper.getRoleInfo(studyJavaSysRoleDao);
        if (studyJavaSysRoleResultDao != null) {
            throw new StudyJavaException("角色编码已存在");
        }
        studyJavaSysRoleDao = convertToDao(roleDto);
        studyJavaSysRoleDao.setDelFlag(0);
        return studyJavaSysRoleMapper.insertRole(studyJavaSysRoleDao) > 0;
    }

    @Override
    @Transactional
    public boolean updateRole(StudyJavaSysRoleDto roleDto) {
        StudyJavaSysRoleDao role = convertToDao(roleDto);

        // 1. 删除旧的角色-菜单关系
        studyJavaSysRoleMapper.deleteRoleMenusByRoleId(roleDto.getId());

        // 2. 批量插入新的角色-菜单关系
        if (roleDto.getMenuIds() != null && !roleDto.getMenuIds().isEmpty()) {
            studyJavaSysRoleMapper.insertRoleMenus(roleDto.getId(), roleDto.getMenuIds());
        }
        // 3. 更新角色主表
        return studyJavaSysRoleMapper.updateRole(role) > 0;
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
    public StudyJavaSysRoleVo getRoleInfo(Long id) {
        StudyJavaSysRoleDao studyJavaSysRoleDao= new StudyJavaSysRoleDao();
        studyJavaSysRoleDao.setId(id);
        return convertToVo(studyJavaSysRoleMapper.getRoleInfo(studyJavaSysRoleDao));
    }

    /**
     * 将 Dao 对象转换为 Vo 对象
     */
    private StudyJavaSysRoleVo convertToVo(StudyJavaSysRoleDao dao) {
        if (dao == null) {
            return null;
        }
        StudyJavaSysRoleVo vo = new StudyJavaSysRoleVo();
        BeanUtils.copyProperties(dao, vo);
        return vo;
    }

    /**
     * 将 Dto 对象转换为 Dao 对象
     */
    private StudyJavaSysRoleDao convertToDao(StudyJavaSysRoleDto dto) {
        if (dto == null) {
            return null;
        }
        StudyJavaSysRoleDao dao = new StudyJavaSysRoleDao();
        BeanUtils.copyProperties(dto, dao);
        return dao;
    }
}
