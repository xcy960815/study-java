package com.example.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.domain.StudyJavaUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @author opera
* @description 针对表【study_java_user】的数据库操作Mapper
* @createDate 2024-10-16 11:26:58
* @Entity com.example.domain.StudyJavaUser
*/
@Repository
public interface StudyJavaUserMapper {

//    int insert(StudyJavaUser record);
//
//    int insertSelective(StudyJavaUser record);
//
//    int updateById(StudyJavaUser record);
//
//    int updateByPrimaryKey(StudyJavaUser record);

    // 获取用户列表
    IPage<StudyJavaUser> getUserList(@Param("page") Page<StudyJavaUser> page, @Param("userQueryData") StudyJavaUser userQueryData);
}
