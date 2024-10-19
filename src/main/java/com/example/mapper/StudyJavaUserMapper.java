package com.example.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.domain.StudyJavaUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


/**
* @author opera
* 针对表【study_java_user】的数据库操作Mapper
* 2024-10-16 11:26:58
* com.example.domain.StudyJavaUser
*/

@Repository
public interface StudyJavaUserMapper {
    // 获取用户列表
    IPage<StudyJavaUser> getUserList(@Param("page") Page<StudyJavaUser> page, @Param("studyJavaUser") StudyJavaUser studyJavaUser);
    // 更新用户
    int updateUser( @Param("studyJavaUser") StudyJavaUser studyJavaUser);
}