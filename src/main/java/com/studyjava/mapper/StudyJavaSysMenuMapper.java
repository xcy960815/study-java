package com.studyjava.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.studyjava.domain.dao.StudyJavaSysMenuDao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author opera
* @description 针对表【study_java_sys_menu(系统菜单表)】的数据库操作Mapper
* @createDate 2025-05-07 17:46:58
* @Entity com.example.domain.dao.StudyJavaSysMenuDao
*/
public interface StudyJavaSysMenuMapper extends BaseMapper<StudyJavaSysMenuDao> {

    /**
     * 获取菜单列表
     * @param page Page<StudyJavaSysMenuDao>
     * @param studyJavaSysMenuDao StudyJavaSysMenuDao
     * @return IPage<StudyJavaSysMenuDao>
     */
    IPage<StudyJavaSysMenuDao>getMenuList(@Param("page")  IPage<StudyJavaSysMenuDao> page, @Param("studyJavaSysMenuDao") StudyJavaSysMenuDao studyJavaSysMenuDao);

    /**
     * 获取所有菜单树
     * @return List<StudyJavaSysMenuDao>
     */
    List<StudyJavaSysMenuDao> getAllMenuList();

    /**
     * 通过主键查询单条数据
     *
     * @param studyJavaSysMenuDao StudyJavaSysMenuDao
     * @return 单条数据
     */
    StudyJavaSysMenuDao getMenuInfo(@Param("studyJavaSysMenuDao") StudyJavaSysMenuDao studyJavaSysMenuDao);

    /**
     * 获取当前用户所拥有的菜单
     * @param userId Long
     * @return List<StudyJavaSysMenuDao>
     */
    List<StudyJavaSysMenuDao> getRoutes(@Param("userId") Long userId);

    /**
     * 新增菜单
     *
     * @param studyJavaSysMenuDao 菜单信息
     * @return 新增结果
     */
    int insertMenu(@Param("studyJavaSysMenuDao") StudyJavaSysMenuDao studyJavaSysMenuDao);

    /**
     * 更新菜单
     *
     * @param studyJavaSysMenuDao 菜单信息
     * @return 更新结果
     */
    int updateMenu(@Param("studyJavaSysMenuDao") StudyJavaSysMenuDao studyJavaSysMenuDao);

    /**
     * 删除菜单（软删除）
     *
     * @param studyJavaSysMenuDao 菜单信息
     * @return 删除结果
     */
    int deleteMenu(@Param("studyJavaSysMenuDao") StudyJavaSysMenuDao studyJavaSysMenuDao);

    /**
     * 删除角色-菜单关联（根据菜单ID）
     */
    int deleteRoleMenusByMenuId(@Param("menuId") Long menuId);
}




