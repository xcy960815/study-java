package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.domain.dao.StudyJavaSysMenuDao;
import com.example.domain.vo.StudyJavaSysMenuVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统菜单表(StudyJavaSysMenuDao)表数据库访问层
 *
 * @author makejava
 * @since 2025-04-25 18:27:53
 */
public interface StudyJavaSysMenuMapper extends BaseMapper<StudyJavaSysMenuDao> {
    
    /**
     * 查询菜单列表
     *
     * @param menuName 菜单名称
     * @param menuType 菜单类型
     * @param parentId 父菜单ID
     * @return 菜单列表
     */
    List<StudyJavaSysMenuVo> selectMenuList(
        @Param("menuName") String menuName,
        @Param("menuType") Integer menuType,
        @Param("parentId") Long parentId
    );

    /**
     * 新增菜单
     *
     * @param menu 菜单信息
     * @return 影响行数
     */
    int insertMenu(StudyJavaSysMenuDao menu);

    /**
     * 更新菜单
     *
     * @param menu 菜单信息
     * @return 影响行数
     */
    int updateMenu(StudyJavaSysMenuDao menu);

    /**
     * 删除菜单
     *
     * @param menuId 菜单ID
     * @return 影响行数
     */
    int deleteMenu(@Param("menuId") Long menuId);

    /**
     * 查询菜单详情
     *
     * @param menuId 菜单ID
     * @return 菜单详情
     */
    StudyJavaSysMenuVo selectMenuById(@Param("menuId") Long menuId);
}

