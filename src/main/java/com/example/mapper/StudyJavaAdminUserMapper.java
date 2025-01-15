package com.example.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.domain.dao.StudyJavaAdminUserDao;
import com.example.domain.vo.StudyJavaAdminUserVo;
import org.apache.ibatis.annotations.Param;

/**
* @author opera
* @description 针对表【study_java_admin_user】的数据库操作Mapper
* @createDate 2024-10-22 20:57:35
* @Entity com.example.domain.dao.StudyJavaAdminUserDao
*/
public interface StudyJavaAdminUserMapper {
    // 获取所有的超级管理员列表
    IPage<StudyJavaAdminUserDao> getAdminUserList(@Param("page") Page<StudyJavaAdminUserVo> page, @Param("studyJavaAdminUser") StudyJavaAdminUserVo studyJavaAdminUser);

    // 更新管理员信息
    int updateAdminUser(@Param("studyJavaAdminUser") StudyJavaAdminUserVo studyJavaAdminUser);

    // 添加管理员
    int insertAdminUser(@Param("studyJavaAdminUser") StudyJavaAdminUserVo studyJavaAdminUser);

    // 软删除管理员
    int deleteAdminUser(@Param("studyJavaAdminUser") StudyJavaAdminUserVo studyJavaAdminUser);
}
