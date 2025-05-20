package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.domain.dao.StudyJavaSysMenuDao;
import com.example.domain.dto.StudyJavaSysMenuDto;
import com.example.domain.vo.StudyJavaSysMenuVo;
import com.example.service.StudyJavaSysMenuService;
import com.example.mapper.StudyJavaSysMenuMapper;
import com.example.exception.StudyJavaException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 系统菜单服务实现类
 * @author opera
 */
@Slf4j
@Service
public class StudyJavaSysMenuServiceImpl implements StudyJavaSysMenuService {

    @Resource
    private StudyJavaSysMenuMapper studyJavaSysMenuMapper;

    /**
     * 将 DAO 对象转换为 DTO 对象
     * @param menuDao DAO对象
     * @return DTO对象
     */
    private StudyJavaSysMenuDto convertToDto(StudyJavaSysMenuDao menuDao) {
        if (menuDao == null) {
            return null;
        }
        StudyJavaSysMenuDto dto = new StudyJavaSysMenuDto();
        dto.setMenuId(menuDao.getMenuId());
        dto.setMenuName(menuDao.getMenuName());
        dto.setComponent(menuDao.getComponent());
        dto.setMenuType(menuDao.getMenuType());
        dto.setIcon(menuDao.getIcon());
        dto.setCreateTime(menuDao.getCreateTime());
        dto.setOrderNum(menuDao.getOrderNum());
        return dto;
    }

    /**
     * 将 Dto 对象转换为 DAO 对象
     * @param studyJavaSysMenuDto VO对象
     * @return DAO对象
     */
    private StudyJavaSysMenuDao convertToDao(StudyJavaSysMenuDto studyJavaSysMenuDto) {
        if (studyJavaSysMenuDto == null) {
            return null;
        }
        StudyJavaSysMenuDao studyJavaSysMenuDao = new StudyJavaSysMenuDao();
        studyJavaSysMenuDao.setMenuName(studyJavaSysMenuDto.getMenuName());
        studyJavaSysMenuDao.setParentId(studyJavaSysMenuDto.getParentId());
        studyJavaSysMenuDao.setPath(studyJavaSysMenuDto.getPath());
        studyJavaSysMenuDao.setComponent(studyJavaSysMenuDto.getComponent());
        studyJavaSysMenuDao.setIcon(studyJavaSysMenuDto.getIcon());
        studyJavaSysMenuDao.setMenuType(studyJavaSysMenuDto.getMenuType());
        studyJavaSysMenuDao.setPerms(studyJavaSysMenuDto.getPerms());
        studyJavaSysMenuDao.setOrderNum(studyJavaSysMenuDto.getOrderNum());
        return studyJavaSysMenuDao;
    }

    private StudyJavaSysMenuVo convertToVo(StudyJavaSysMenuDao studyJavaSysMenuDao) {
        if (studyJavaSysMenuDao == null) {
            return null;
        }
        StudyJavaSysMenuVo studyJavaSysMenuVo = new StudyJavaSysMenuVo();
        studyJavaSysMenuVo.setMenuId(studyJavaSysMenuDao.getMenuId());
        studyJavaSysMenuVo.setMenuName(studyJavaSysMenuDao.getMenuName());
        studyJavaSysMenuVo.setParentId(studyJavaSysMenuDao.getParentId());
        studyJavaSysMenuVo.setPath(studyJavaSysMenuDao.getPath());
        studyJavaSysMenuVo.setComponent(studyJavaSysMenuDao.getComponent());
        studyJavaSysMenuVo.setIcon(studyJavaSysMenuDao.getIcon());
        studyJavaSysMenuVo.setMenuType(studyJavaSysMenuDao.getMenuType());
        studyJavaSysMenuVo.setPerms(studyJavaSysMenuDao.getPerms());
        studyJavaSysMenuVo.setOrderNum(studyJavaSysMenuDao.getOrderNum());
        return studyJavaSysMenuVo;
    }

    @Override
    public IPage<StudyJavaSysMenuVo> getMenuList(Page<StudyJavaSysMenuDto> page, StudyJavaSysMenuDto studyJavaSysMenuDto) {
        try {
            Page<StudyJavaSysMenuDao> studyJavaSysMenuDaoPage = new Page<>(page.getCurrent(), page.getSize(),page.getTotal());
            StudyJavaSysMenuDao studyJavaSysMenuDao = convertToDao(studyJavaSysMenuDto);
            IPage<StudyJavaSysMenuDao> menuDaoIPage = studyJavaSysMenuMapper.getMenuList(studyJavaSysMenuDaoPage, studyJavaSysMenuDao);
            List<StudyJavaSysMenuVo> menuVoList = menuDaoIPage.getRecords().stream()
                    .map(this::convertToVo)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            Page<StudyJavaSysMenuVo> studyJavaSysMenuVoPage = new Page<>(page.getCurrent(), page.getSize(),page.getTotal());
            studyJavaSysMenuVoPage.setRecords(menuVoList);
            return studyJavaSysMenuVoPage;
        } catch (Exception e) {
            log.error("获取菜单列表失败", e);
            throw new StudyJavaException("获取菜单列表失败");
        }
    }

    @Override
    public StudyJavaSysMenuVo getMenuDetail(Serializable id) {
        Assert.notNull(id, "菜单ID不能为空");
        try {
            StudyJavaSysMenuDao studyJavaSysMenuDao = studyJavaSysMenuMapper.getMenuDetail(id);
            return convertToVo(studyJavaSysMenuDao);
        } catch (Exception e) {
            log.error("获取菜单详情失败, id: {}", id, e);
            throw new StudyJavaException("获取菜单详情失败");
        }
    }

    @Override
    public Boolean addMenu(StudyJavaSysMenuDto studyJavaSysMenuDto) {
        Assert.notNull(studyJavaSysMenuDto, "菜单信息不能为空");
        try {
            StudyJavaSysMenuDao menuDao = convertToDao(studyJavaSysMenuDto);
            menuDao.setCreateTime(new Date());
            menuDao.setUpdateTime(new Date());
            menuDao.setIsDeleted(0);
            return studyJavaSysMenuMapper.addMenu(menuDao) > 0;
        } catch (Exception e) {
            log.error("添加菜单失败", e);
            throw new StudyJavaException("添加菜单失败");
        }
    }

    @Override
    public Boolean updateMenu(StudyJavaSysMenuDto studyJavaSysMenuDto) {
        Assert.notNull(studyJavaSysMenuDto, "菜单信息不能为空");
        Assert.notNull(studyJavaSysMenuDto.getMenuId(), "菜单ID不能为空");
        try {
            StudyJavaSysMenuDao menuDao = convertToDao(studyJavaSysMenuDto);
            menuDao.setMenuId(studyJavaSysMenuDto.getMenuId());
            menuDao.setUpdateTime(new Date());
            return studyJavaSysMenuMapper.updateMenu(menuDao) > 0;
        } catch (Exception e) {
            log.error("更新菜单失败, id: {}", studyJavaSysMenuDto.getMenuId(), e);
            throw new StudyJavaException("更新菜单失败");
        }
    }

    @Override
    public Boolean deleteMenu(Serializable id) {
        Assert.notNull(id, "菜单ID不能为空");
        try {
            StudyJavaSysMenuDao studyJavaSysMenuDao = new StudyJavaSysMenuDao();
            studyJavaSysMenuDao.setMenuId((Long) id);
            studyJavaSysMenuDao.setIsDeleted(1);
            studyJavaSysMenuDao.setUpdateTime(new Date());

            return studyJavaSysMenuMapper.deleteMenu(studyJavaSysMenuDao) > 0;
        } catch (Exception e) {
            log.error("删除菜单失败, id: {}", id, e);
            throw new StudyJavaException("删除菜单失败");
        }
    }
}




