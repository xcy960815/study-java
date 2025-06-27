package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.domain.dao.StudyJavaSysMenuDao;
import com.example.domain.dto.StudyJavaSysMenuDto;
import com.example.domain.vo.StudyJavaSysMenuVo;
import com.example.domain.vo.StudyJavaSysUserVo;
import com.example.service.StudyJavaSysMenuService;
import com.example.mapper.StudyJavaSysMenuMapper;
import com.example.exception.StudyJavaException;
import com.example.service.StudyJavaSysUserService;
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

    @Resource
    private StudyJavaSysMenuMapper studyJavaSysMenuMapper;

    @Resource
    private StudyJavaSysUserService studyJavaSysUserService;

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
     * @param studyJavaSysMenuDao StudyJavaSysMenuDao
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
    /**
     * 获取菜单列表
     * @param page Page<StudyJavaSysMenuDto>
     * @param studyJavaSysMenuDto StudyJavaSysMenuDto
     * @return IPage<StudyJavaSysMenuVo>
     */
    @Override
    public IPage<StudyJavaSysMenuVo> getMenuList(IPage<StudyJavaSysMenuDao> studyJavaSysMenuDaoPage, StudyJavaSysMenuDto studyJavaSysMenuDto) {
        try {
            StudyJavaSysMenuDao studyJavaSysMenuDao = convertToDao(studyJavaSysMenuDto);
            IPage<StudyJavaSysMenuDao> menuDaoIPage = studyJavaSysMenuMapper.getMenuList(studyJavaSysMenuDaoPage, studyJavaSysMenuDao);
            List<StudyJavaSysMenuVo> menuVoList = menuDaoIPage.getRecords().stream()
                    .map(this::convertToVo)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
           IPage<StudyJavaSysMenuVo> studyJavaSysMenuVoPage = new Page<>(studyJavaSysMenuDaoPage.getCurrent(), studyJavaSysMenuDaoPage.getSize(), studyJavaSysMenuDaoPage.getTotal());
            studyJavaSysMenuVoPage.setRecords(menuVoList);
            return studyJavaSysMenuVoPage;
        } catch (Exception e) {
            log.error("获取菜单列表失败", e);
            throw new StudyJavaException("获取菜单列表失败");
        }
    }

    /**
     * 获取所有菜单列表
     * @return List<StudyJavaSysMenuVo>
     */
    @Override
    public List<StudyJavaSysMenuVo> getAllMenuList(){
        List<StudyJavaSysMenuDao> menuDaoList = studyJavaSysMenuMapper.getAllMenuList();
        return menuDaoList.stream()
                .map(this::convertToVo)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * 获取菜单详情
     * @param id Serializable
     * @return StudyJavaSysMenuVo
     */
    @Override
    public StudyJavaSysMenuVo getMenuInfo(Serializable id) {
        Assert.notNull(id, "菜单ID不能为空");
        try {
            StudyJavaSysMenuDao studyJavaSysRequestMenuDao = new StudyJavaSysMenuDao();
            studyJavaSysRequestMenuDao.setId(Long.valueOf(id.toString()));
            StudyJavaSysMenuDao studyJavaSysMenuDao = studyJavaSysMenuMapper.getMenuInfo(studyJavaSysRequestMenuDao);
            return convertToVo(studyJavaSysMenuDao);
        } catch (Exception e) {
            log.error("获取菜单详情失败, id: {}", id, e);
            throw new StudyJavaException("获取菜单详情失败");
        }
    }

    /**
     * 获取当前用户下面所对应的菜单
     * @return List<StudyJavaSysMenuVo>
     */
    @Override
    public List<StudyJavaSysMenuVo> getRoutes() {
        StudyJavaSysUserVo studyJavaSysUserVo = studyJavaSysUserService.getUserInfo();
        List<StudyJavaSysMenuDao> studyJavaSysMenuDaoList = studyJavaSysMenuMapper.getRoutes(studyJavaSysUserVo.getId());
        return studyJavaSysMenuDaoList.stream()
                .map(this::convertToVo)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
    /**
     * 添加菜单
     * @param studyJavaSysMenuDto StudyJavaSysMenuDto
     * @return Boolean
     */
    @Override
    public Boolean insertMenu(StudyJavaSysMenuDto studyJavaSysMenuDto) {
        try {
            StudyJavaSysMenuDao studyJavaSysMenuDao = convertToDao(studyJavaSysMenuDto);
            studyJavaSysMenuDao.setCreateTime(new Date());
            studyJavaSysMenuDao.setUpdateTime(new Date());
            studyJavaSysMenuDao.setIsDeleted(0);
            return studyJavaSysMenuMapper.insertMenu(studyJavaSysMenuDao) > 0;
        } catch (Exception e) {
            throw new StudyJavaException("菜单添加失败");
        }
    }
    /**
     * 更新菜单
     * @param studyJavaSysMenuDto StudyJavaSysMenuDto
     * @return Boolean
     */
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
            throw new StudyJavaException("菜单更新失败");
        }
    }
    /**
     * 删除菜单
     * @param studyJavaSysMenuDto StudyJavaSysMenuDto
     * @return Boolean
     */
    @Override
    @Transactional
    public Boolean deleteMenu(StudyJavaSysMenuDto studyJavaSysMenuDto) {
        Assert.notNull(studyJavaSysMenuDto.getId(), "菜单ID不能为空");
        // 先删除角色-菜单关联
        studyJavaSysMenuMapper.deleteRoleMenusByMenuId(studyJavaSysMenuDto.getId());
        StudyJavaSysMenuDao studyJavaSysMenuDao = new StudyJavaSysMenuDao();
        studyJavaSysMenuDao.setId(studyJavaSysMenuDto.getId());
        studyJavaSysMenuDao.setIsDeleted(ISMENUDELETE);
        studyJavaSysMenuDao.setUpdateTime(new Date());
        return studyJavaSysMenuMapper.deleteMenu(studyJavaSysMenuDao) > 0;
    }
}




