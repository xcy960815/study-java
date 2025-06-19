package com.example.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.domain.dao.StudyJavaSysMenuDao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.domain.dto.StudyJavaSysMenuDto;
import com.example.domain.vo.StudyJavaSysMenuVo;
import org.apache.ibatis.annotations.Param;
import java.io.Serializable;

/**
* @author opera
* @description 针对表【study_java_sys_menu(系统菜单表)】的数据库操作Mapper
* @createDate 2025-05-07 17:46:58
* @Entity com.example.domain.dao.StudyJavaSysMenuDao
*/
public interface StudyJavaSysMenuMapper extends BaseMapper<StudyJavaSysMenuDao> {

    IPage<StudyJavaSysMenuDao>getMenuList(Page<StudyJavaSysMenuDao> page, @Param("studyJavaSysMenuDao") StudyJavaSysMenuDao studyJavaSysMenuDao);

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    StudyJavaSysMenuDao getMenuDetail(@Param("id") Serializable id);

    /**
     * 新增菜单
     *
     * @param studyJavaSysMenuDao 菜单信息
     * @return 新增结果
     */
    int addMenu(@Param("studyJavaSysMenuDao") StudyJavaSysMenuDao studyJavaSysMenuDao);

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




