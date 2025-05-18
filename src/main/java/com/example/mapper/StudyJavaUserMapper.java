package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.domain.dao.StudyJavaUserDao;
import com.example.domain.dto.StudyJavaUserDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


/**
* @author opera
* 针对表【study_java_user】的数据库操作Mapper
* 2024-10-16 11:26:58
* com.example.domain.StudyJavaUser
*/

@Repository
public interface StudyJavaUserMapper extends BaseMapper<StudyJavaUserDao> {
    // 获取用户列表
    IPage<StudyJavaUserDao> getUserList(@Param("page") Page<StudyJavaUserDto> page, @Param("studyJavaUser") StudyJavaUserDto studyJavaUser);
    // 更新用户
    int updateUserInfo(@Param("studyJavaUser") StudyJavaUserDto studyJavaUser);
    // 添加用户
    int insertUserInfo(@Param("studyJavaUser") StudyJavaUserDto studyJavaUser);
    // 删除用户
    int deleteUserInfo(@Param("studyJavaUser") StudyJavaUserDto studyJavaUser);
    // 更新用户头像
    int updateUserAvatar(@Param("userId") String userId, @Param("base64ImageUrl") String base64ImageUrl);
    // 查找用户信息
    StudyJavaUserDao getUserInfo(@Param("studyJavaUser") StudyJavaUserDto studyJavaUser);
}
