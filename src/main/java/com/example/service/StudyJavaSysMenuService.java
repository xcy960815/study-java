package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.domain.dao.StudyJavaSysMenuDao;
import com.example.domain.dto.StudyJavaSysMenuDto;

import java.util.List;

/**
 * 系统菜单表(StudyJavaSysMenuDao)表服务接口
 *
 * @author makejava
 * @since 2025-04-25 18:27:54
 */
public interface StudyJavaSysMenuService extends IService<StudyJavaSysMenuDao> {

    /**
     * 获取菜单列表
     *
     * @param menuName 菜单名称
     * @param menuType 菜单类型
     * @param parentId 父菜单ID
     * @return 菜单列表
     */
    List<StudyJavaSysMenuDto> getMenuList(String menuName, Integer menuType, Long parentId);
    
    /**
     * 根据ID获取菜单
     *
     * @param menuId 菜单ID
     * @return 菜单信息
     */
    StudyJavaSysMenuDto getMenuById(Long menuId);
    
    /**
     * 新增菜单
     *
     * @param menuDto 菜单信息
     */
    void addMenu(StudyJavaSysMenuDto menuDto);
    
    /**
     * 更新菜单
     *
     * @param menuDto 菜单信息
     */
    void updateMenu(StudyJavaSysMenuDto menuDto);
    
    /**
     * 删除菜单
     *
     * @param menuId 菜单ID
     */
    void deleteMenu(Long menuId);
    
    /**
     * 获取用户菜单列表
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<StudyJavaSysMenuDto> getUserMenuList(Long userId);
}

