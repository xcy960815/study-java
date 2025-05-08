package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.domain.dao.StudyJavaSysMenuDao;
import com.example.domain.dto.StudyJavaSysMenuDto;
import com.example.domain.vo.StudyJavaSysMenuVo;
import com.example.service.StudyJavaSysMenuService;
import com.example.mapper.StudyJavaSysMenuMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author opera
* @description 针对表【study_java_sys_menu(系统菜单表)】的数据库操作Service实现
* @createDate 2025-05-07 17:46:58
*/
@Service
public class StudyJavaSysMenuServiceImpl implements StudyJavaSysMenuService{
    @Resource
    private StudyJavaSysMenuMapper studyJavaSysMenuMapper;

     private StudyJavaSysMenuDto makeDaoToDto(StudyJavaSysMenuDao studyJavaSysMenuDao) {StudyJavaSysMenuDto studyJavaSysMenuDto = new StudyJavaSysMenuDto();studyJavaSysMenuDto.setMenuId(studyJavaSysMenuDao.getMenuId());
        studyJavaSysMenuDto.setMenuName(studyJavaSysMenuDao.getMenuName());
        studyJavaSysMenuDto.setComponent(studyJavaSysMenuDao.getComponent());
        studyJavaSysMenuDto.setMenuType(studyJavaSysMenuDao.getMenuType());
        studyJavaSysMenuDto.setIcon(studyJavaSysMenuDao.getIcon());
        studyJavaSysMenuDto.setCreateTime(studyJavaSysMenuDao.getCreateTime());
        studyJavaSysMenuDto.setOrderNum(studyJavaSysMenuDao.getOrderNum());
        return studyJavaSysMenuDto;
     }
    public IPage<StudyJavaSysMenuDto> list(Page<StudyJavaSysMenuVo> page , StudyJavaSysMenuVo studyJavaSysMenuVo){
        IPage<StudyJavaSysMenuDao> menuDaoIPage = studyJavaSysMenuMapper.list(page,studyJavaSysMenuVo);
        List<StudyJavaSysMenuDto> menuDtoList = menuDaoIPage.getRecords().stream().map(this::makeDaoToDto).collect(Collectors.toList());
        IPage<StudyJavaSysMenuDto> menuDtoIPage = new Page<>(menuDaoIPage.getCurrent(), menuDaoIPage.getSize(), menuDaoIPage.getTotal());
        menuDtoIPage.setRecords(menuDtoList);
        return menuDtoIPage;
     }

    @Override
    public StudyJavaSysMenuDto getDetail(Serializable id) {
        StudyJavaSysMenuDao menuDao = studyJavaSysMenuMapper.getDetail(id);
        if (menuDao == null) {
            return null;
        }
        return makeDaoToDto(menuDao);
    }

    @Override
    public Boolean addMenu(StudyJavaSysMenuVo studyJavaSysMenuVo) {
        StudyJavaSysMenuDao menuDao = new StudyJavaSysMenuDao();
        menuDao.setMenuName(studyJavaSysMenuVo.getMenuName());
        menuDao.setParentId(studyJavaSysMenuVo.getParentId());
        menuDao.setPath(studyJavaSysMenuVo.getPath());
        menuDao.setComponent(studyJavaSysMenuVo.getComponent());
        menuDao.setIcon(studyJavaSysMenuVo.getIcon());
        menuDao.setMenuType(studyJavaSysMenuVo.getMenuType());
        menuDao.setPerms(studyJavaSysMenuVo.getPerms());
        menuDao.setOrderNum(studyJavaSysMenuVo.getOrderNum());
        menuDao.setCreateTime(new Date());
        menuDao.setUpdateTime(new Date());
        menuDao.setIsDeleted(0);

        return studyJavaSysMenuMapper.addMenu(menuDao) > 0;
    }

    @Override
    public Boolean updateMenu(StudyJavaSysMenuVo studyJavaSysMenuVo) {
        StudyJavaSysMenuDao menuDao = new StudyJavaSysMenuDao();
        menuDao.setMenuId(studyJavaSysMenuVo.getMenuId());
        menuDao.setMenuName(studyJavaSysMenuVo.getMenuName());
        menuDao.setParentId(studyJavaSysMenuVo.getParentId());
        menuDao.setPath(studyJavaSysMenuVo.getPath());
        menuDao.setComponent(studyJavaSysMenuVo.getComponent());
        menuDao.setIcon(studyJavaSysMenuVo.getIcon());
        menuDao.setMenuType(studyJavaSysMenuVo.getMenuType());
        menuDao.setPerms(studyJavaSysMenuVo.getPerms());
        menuDao.setOrderNum(studyJavaSysMenuVo.getOrderNum());
        menuDao.setUpdateTime(new Date());

        return studyJavaSysMenuMapper.updateMenu(menuDao) > 0;
    }

    @Override
    public Boolean deleteMenu(Serializable id) {
        StudyJavaSysMenuDao menuDao = new StudyJavaSysMenuDao();
        menuDao.setMenuId((Long) id);
        menuDao.setIsDeleted(1);
        menuDao.setUpdateTime(new Date());

        return studyJavaSysMenuMapper.deleteMenu(menuDao) > 0;
    }
}




