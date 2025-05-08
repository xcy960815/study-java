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
import org.springframework.transaction.annotation.Transactional;
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
     * 将 VO 对象转换为 DAO 对象
     * @param vo VO对象
     * @return DAO对象
     */
    private StudyJavaSysMenuDao convertToDao(StudyJavaSysMenuVo vo) {
        if (vo == null) {
            return null;
        }

        StudyJavaSysMenuDao dao = new StudyJavaSysMenuDao();
        dao.setMenuName(vo.getMenuName());
        dao.setParentId(vo.getParentId());
        dao.setPath(vo.getPath());
        dao.setComponent(vo.getComponent());
        dao.setIcon(vo.getIcon());
        dao.setMenuType(vo.getMenuType());
        dao.setPerms(vo.getPerms());
        dao.setOrderNum(vo.getOrderNum());
        return dao;
    }

    @Override
    public IPage<StudyJavaSysMenuDto> list(Page<StudyJavaSysMenuVo> page, StudyJavaSysMenuVo studyJavaSysMenuVo) {
        try {
            IPage<StudyJavaSysMenuDao> menuDaoIPage = studyJavaSysMenuMapper.list(page, studyJavaSysMenuVo);
            List<StudyJavaSysMenuDto> menuDtoList = menuDaoIPage.getRecords().stream()
                    .map(this::convertToDto)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            IPage<StudyJavaSysMenuDto> menuDtoIPage = new Page<>(menuDaoIPage.getCurrent(), 
                    menuDaoIPage.getSize(), menuDaoIPage.getTotal());
            menuDtoIPage.setRecords(menuDtoList);
            return menuDtoIPage;
        } catch (Exception e) {
            log.error("获取菜单列表失败", e);
            throw new StudyJavaException("获取菜单列表失败");
        }
    }

    @Override
    public StudyJavaSysMenuDto getDetail(Serializable id) {
        Assert.notNull(id, "菜单ID不能为空");
        try {
            StudyJavaSysMenuDao menuDao = studyJavaSysMenuMapper.getDetail(id);
            return convertToDto(menuDao);
        } catch (Exception e) {
            log.error("获取菜单详情失败, id: {}", id, e);
            throw new StudyJavaException("获取菜单详情失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addMenu(StudyJavaSysMenuVo studyJavaSysMenuVo) {
        Assert.notNull(studyJavaSysMenuVo, "菜单信息不能为空");
        try {
            StudyJavaSysMenuDao menuDao = convertToDao(studyJavaSysMenuVo);
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
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateMenu(StudyJavaSysMenuVo studyJavaSysMenuVo) {
        Assert.notNull(studyJavaSysMenuVo, "菜单信息不能为空");
        Assert.notNull(studyJavaSysMenuVo.getMenuId(), "菜单ID不能为空");
        try {
            StudyJavaSysMenuDao menuDao = convertToDao(studyJavaSysMenuVo);
            menuDao.setMenuId(studyJavaSysMenuVo.getMenuId());
            menuDao.setUpdateTime(new Date());

            return studyJavaSysMenuMapper.updateMenu(menuDao) > 0;
        } catch (Exception e) {
            log.error("更新菜单失败, id: {}", studyJavaSysMenuVo.getMenuId(), e);
            throw new StudyJavaException("更新菜单失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteMenu(Serializable id) {
        Assert.notNull(id, "菜单ID不能为空");
        try {
            StudyJavaSysMenuDao menuDao = new StudyJavaSysMenuDao();
            menuDao.setMenuId((Long) id);
            menuDao.setIsDeleted(1);
            menuDao.setUpdateTime(new Date());

            return studyJavaSysMenuMapper.deleteMenu(menuDao) > 0;
        } catch (Exception e) {
            log.error("删除菜单失败, id: {}", id, e);
            throw new StudyJavaException("删除菜单失败");
        }
    }
}




