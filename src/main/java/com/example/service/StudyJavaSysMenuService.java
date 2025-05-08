package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.domain.dto.StudyJavaSysMenuDto;
import com.example.domain.vo.StudyJavaSysMenuVo;
import java.io.Serializable;

/**
* @author opera
* @description 针对表【study_java_sys_menu(系统菜单表)】的数据库操作Service
* @createDate 2025-05-07 17:46:58
*/
public interface StudyJavaSysMenuService {
    IPage<StudyJavaSysMenuDto> list (Page<StudyJavaSysMenuVo> page, StudyJavaSysMenuVo menuQueryData);

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    StudyJavaSysMenuDto getDetail(Serializable id);

    /**
     * 新增菜单
     *
     * @param studyJavaSysMenuVo 菜单信息
     * @return 新增结果
     */
    Boolean addMenu(StudyJavaSysMenuVo studyJavaSysMenuVo);

    /**
     * 更新菜单
     *
     * @param studyJavaSysMenuVo 菜单信息
     * @return 更新结果
     */
    Boolean updateMenu(StudyJavaSysMenuVo studyJavaSysMenuVo);

    /**
     * 删除菜单（软删除）
     *
     * @param id 菜单ID
     * @return 删除结果
     */
    Boolean deleteMenu(Serializable id);
}
