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
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统菜单服务实现类
 * @author opera
 */
@Slf4j
@Service
public class StudyJavaSysMenuServiceImpl implements StudyJavaSysMenuService {
    private final int ISMENUDELETE = 1;
    private final int ISMENUEDIT = 0;

    @Resource
    private StudyJavaSysMenuMapper studyJavaSysMenuMapper;


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
//        studyJavaSysMenuDao.setCreateTime(studyJavaSysMenuDto.getCreateTime());
//        studyJavaSysMenuDao.setUpdateTime(studyJavaSysMenuDto.getUpdateTime());
        return studyJavaSysMenuDao;
    }

    /**
     * 将 DAO 对象转换为 VO 对象
     * @param studyJavaSysMenuDao
     * @return VO对象
     */
    private StudyJavaSysMenuVo convertToVo(StudyJavaSysMenuDao studyJavaSysMenuDao) {
        if (studyJavaSysMenuDao == null) {
            return null;
        }
        StudyJavaSysMenuVo studyJavaSysMenuVo = new StudyJavaSysMenuVo();
        studyJavaSysMenuVo.setId(studyJavaSysMenuDao.getId());
        studyJavaSysMenuVo.setMenuName(studyJavaSysMenuDao.getMenuName());
        studyJavaSysMenuVo.setParentId(studyJavaSysMenuDao.getParentId());
        studyJavaSysMenuVo.setPath(studyJavaSysMenuDao.getPath());
        studyJavaSysMenuVo.setComponent(studyJavaSysMenuDao.getComponent());
        studyJavaSysMenuVo.setIcon(studyJavaSysMenuDao.getIcon());
        studyJavaSysMenuVo.setMenuType(studyJavaSysMenuDao.getMenuType());
        studyJavaSysMenuVo.setPerms(studyJavaSysMenuDao.getPerms());
        studyJavaSysMenuVo.setOrderNum(studyJavaSysMenuDao.getOrderNum());
        studyJavaSysMenuVo.setCreateTime(studyJavaSysMenuDao.getCreateTime());
        studyJavaSysMenuVo.setUpdateTime(studyJavaSysMenuDao.getUpdateTime());
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
//        Assert.notNull(studyJavaSysMenuDto, "菜单信息不能为空");
        try {
            StudyJavaSysMenuDao studyJavaSysMenuDao = convertToDao(studyJavaSysMenuDto);
            studyJavaSysMenuDao.setCreateTime(new Date());
            studyJavaSysMenuDao.setUpdateTime(new Date());
            studyJavaSysMenuDao.setIsDeleted(0);
            return studyJavaSysMenuMapper.addMenu(studyJavaSysMenuDao) > 0;
        } catch (Exception e) {
            log.error("添加菜单失败", e);
            throw new StudyJavaException("添加菜单失败");
        }
    }

    @Override
    public Boolean updateMenu(StudyJavaSysMenuDto studyJavaSysMenuDto) {
        Assert.notNull(studyJavaSysMenuDto, "菜单信息不能为空");
        Assert.notNull(studyJavaSysMenuDto.getId(), "菜单ID不能为空");
        try {
            StudyJavaSysMenuDao studyJavaSysMenuDao = convertToDao(studyJavaSysMenuDto);
            studyJavaSysMenuDao.setId(studyJavaSysMenuDto.getId());
            studyJavaSysMenuDao.setUpdateTime(new Date());
            return studyJavaSysMenuMapper.updateMenu(studyJavaSysMenuDao) > 0;
        } catch (Exception e) {
            log.error("更新菜单失败, id: {}", studyJavaSysMenuDto.getId(), e);
            throw new StudyJavaException("更新菜单失败");
        }
    }

    @Override
    @Transactional
    public Boolean deleteMenu(Serializable id) {
        Assert.notNull(id, "菜单ID不能为空");
        try {
            Long menuId = Long.valueOf(id.toString());
            // 先删除角色-菜单关联
            studyJavaSysMenuMapper.deleteRoleMenusByMenuId(menuId);
            StudyJavaSysMenuDao studyJavaSysMenuDao = new StudyJavaSysMenuDao();
            studyJavaSysMenuDao.setId(menuId);
            studyJavaSysMenuDao.setIsDeleted(ISMENUDELETE);
            studyJavaSysMenuDao.setUpdateTime(new Date());
            return studyJavaSysMenuMapper.deleteMenu(studyJavaSysMenuDao) > 0;
        } catch (Exception e) {
            log.error("删除菜单失败, id: {}", id, e);
            throw new StudyJavaException("删除菜单失败");
        }
    }
}




