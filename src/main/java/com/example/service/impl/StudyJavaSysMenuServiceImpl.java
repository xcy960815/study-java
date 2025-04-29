package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.domain.dao.StudyJavaSysMenuDao;
import com.example.domain.dto.StudyJavaSysMenuDto;
import com.example.domain.vo.StudyJavaSysMenuVo;
import com.example.mapper.StudyJavaSysMenuMapper;
import com.example.service.StudyJavaSysMenuService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统菜单 Service 实现类
 */
@Service
public class StudyJavaSysMenuServiceImpl extends ServiceImpl<StudyJavaSysMenuMapper, StudyJavaSysMenuDao> implements StudyJavaSysMenuService {

    @Override
    public List<StudyJavaSysMenuDto> getMenuList(String menuName, Integer menuType, Long parentId) {
        List<StudyJavaSysMenuVo> menus = baseMapper.selectMenuList(menuName, menuType, parentId);
        return menus.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public StudyJavaSysMenuDto getMenuById(Long menuId) {
        StudyJavaSysMenuVo menu = baseMapper.selectMenuById(menuId);
        if (menu == null) {
            throw new RuntimeException("菜单不存在");
        }
        return convertToDto(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addMenu(StudyJavaSysMenuDto menuDto) {
        StudyJavaSysMenuDao menu = new StudyJavaSysMenuDao();
        BeanUtils.copyProperties(menuDto, menu);
        int result = baseMapper.insertMenu(menu);
        if (result <= 0) {
            throw new RuntimeException("新增菜单失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMenu(StudyJavaSysMenuDto menuDto) {
        StudyJavaSysMenuVo menu = baseMapper.selectMenuById(menuDto.getMenuId());
        if (menu == null) {
            throw new RuntimeException("菜单不存在");
        }
        StudyJavaSysMenuDao updateMenu = new StudyJavaSysMenuDao();
        BeanUtils.copyProperties(menuDto, updateMenu);
        int result = baseMapper.updateMenu(updateMenu);
        if (result <= 0) {
            throw new RuntimeException("更新菜单失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMenu(Long menuId) {
        StudyJavaSysMenuVo menu = baseMapper.selectMenuById(menuId);
        if (menu == null) {
            throw new RuntimeException("菜单不存在");
        }
        int result = baseMapper.deleteMenu(menuId);
        if (result <= 0) {
            throw new RuntimeException("删除菜单失败");
        }
    }

    @Override
    public List<StudyJavaSysMenuDto> getUserMenuList(Long userId) {
        // TODO: 根据用户ID查询用户角色，然后根据角色查询菜单
        return getMenuList(null, null, null);
    }

    private StudyJavaSysMenuDto convertToDto(StudyJavaSysMenuVo menu) {
        StudyJavaSysMenuDto dto = new StudyJavaSysMenuDto();
        BeanUtils.copyProperties(menu, dto);
        return dto;
    }
}

