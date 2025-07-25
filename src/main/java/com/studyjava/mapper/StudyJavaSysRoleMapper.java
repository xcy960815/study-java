package com.studyjava.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.studyjava.domain.dao.StudyJavaSysRoleDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface StudyJavaSysRoleMapper {

    /**
     * 分页查询角色列表
     */
    IPage<StudyJavaSysRoleDao> getRoleList(@Param("page")  IPage<StudyJavaSysRoleDao> page, @Param("studyJavaSysRoleDao") StudyJavaSysRoleDao studyJavaSysRoleDao);

    /**
     * 查询所有角色列表
     */
    List<StudyJavaSysRoleDao> getAllRoleList();

    /**
     * 新增角色
     */
    int insertRole(@Param("studyJavaSysRoleDao") StudyJavaSysRoleDao studyJavaSysRoleDao);

    /**
     * 修改角色
     */
    int updateRole(@Param("studyJavaSysRoleDao") StudyJavaSysRoleDao studyJavaSysRoleDao);

    /**
     * 删除角色
     */
    int deleteRole(@Param("roleId") Long roleId);

    /**
     * 修改角色状态
     */
    int updateRoleStatus(@Param("studyJavaSysRoleDao") StudyJavaSysRoleDao studyJavaSysRoleDao);

    /**
     * 获取角色信息
     */
    StudyJavaSysRoleDao getRoleInfo(@Param("studyJavaSysRoleDao") StudyJavaSysRoleDao studyJavaSysRoleDao);

    /**
     * 删除角色-菜单关联
     */
    int deleteRoleMenusByRoleId(@Param("roleId") Long roleId);

    /**
     * 批量插入角色-菜单关联
     */
    int insertRoleMenus(@Param("roleId") Long roleId, @Param("menuIds") List<Long> menuIds);

    /**
     * 删除用户-角色关联
     */
    int deleteUserRolesByRoleId(@Param("roleId") Long roleId);
}
