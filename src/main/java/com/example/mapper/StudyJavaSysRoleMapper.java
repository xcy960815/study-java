package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.domain.dao.StudyJavaSysRoleDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StudyJavaSysRoleMapper {

    /**
     * 分页查询角色列表
     */
    IPage<StudyJavaSysRoleDao> getRoleList(IPage<StudyJavaSysRoleDao> page, @Param("studyJavaSysRoleDao") StudyJavaSysRoleDao studyJavaSysRoleDao);

    /**
     * 查询所有角色列表
     */
    List<StudyJavaSysRoleDao> getAllRoleList();

    /**
     * 新增角色
     */
    int addRole(StudyJavaSysRoleDao roleDao);

    /**
     * 修改角色
     */
    int updateRole(StudyJavaSysRoleDao roleDao);

    /**
     * 删除角色
     */
    int deleteRole(Long id);

    /**
     * 修改角色状态
     */
    int updateRoleStatus(StudyJavaSysRoleDao roleDao);

    /**
     * 根据ID获取角色
     */
    StudyJavaSysRoleDao getRoleById(Long id);
}
