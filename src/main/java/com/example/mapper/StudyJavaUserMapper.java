package com.example.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.domain.vo.StudyJavaLoginVo;
import com.example.domain.dao.StudyJavaUserDao;
import com.example.domain.dto.StudyJavaUserDto;
import com.example.domain.vo.StudyJavaUserVo;
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
    IPage<StudyJavaUserDao> getUserList(@Param("page") Page<StudyJavaUserVo> page, @Param("studyJavaUser") StudyJavaUserVo studyJavaUser);
    // 更新用户
    int updateUser( @Param("studyJavaUser") StudyJavaUserVo studyJavaUser);
    // 添加用户
    int insertUser( @Param("studyJavaUser") StudyJavaUserVo studyJavaUser);
    // 删除用户
    int deleteUser( @Param("studyJavaUser") StudyJavaUserVo studyJavaUser);
    // 通过账号密码查找用户信息
    StudyJavaUserDao getUserByNameAndPassword(@Param("studyJavaLoginParams") StudyJavaLoginVo studyJavaLoginParams);
}
