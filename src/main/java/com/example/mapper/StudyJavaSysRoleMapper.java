package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.domain.dao.StudyJavaSysRoleDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StudyJavaSysRoleMapper extends BaseMapper<StudyJavaSysRoleDao> {

    /**
     * 分页查询角色列表
     */
    IPage<StudyJavaSysRoleDao> getRoleList(IPage<StudyJavaSysRoleDao> page, @Param("studyJavaSysRoleDao") StudyJavaSysRoleDao studyJavaSysRoleDao);

    /**
     * 查询所有角色列表
     */
    List<StudyJavaSysRoleDao> getAllRoleList();
}
