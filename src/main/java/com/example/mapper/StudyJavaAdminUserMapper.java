package com.example.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.domain.StudyJavaAdminUser;
import org.apache.ibatis.annotations.Param;

/**
* @author opera
* @description 针对表【study_java_admin_user】的数据库操作Mapper
* @createDate 2024-10-22 20:57:35
* @Entity com.example.domain.StudyJavaAdminUser
*/
public interface StudyJavaAdminUserMapper {
    // 获取所有的超级管理员列表
    IPage<StudyJavaAdminUser> getAdminUserList(@Param("page") Page<StudyJavaAdminUser> page, @Param("studyJavaAdminUser") StudyJavaAdminUser studyJavaAdminUser);

    // 验证用户是否存在
    StudyJavaAdminUser checkUser(@Param("studyJavaAdminUser") StudyJavaAdminUser studyJavaAdminUser);

    // 更新管理员信息
    int updateAdminUser(@Param("studyJavaAdminUser") StudyJavaAdminUser studyJavaAdminUser);

    // 添加管理员
    int insertAdminUser(@Param("studyJavaAdminUser") StudyJavaAdminUser studyJavaAdminUser);

    // 软删除管理员
    int deleteAdminUser(@Param("studyJavaAdminUser") StudyJavaAdminUser studyJavaAdminUser);
}
