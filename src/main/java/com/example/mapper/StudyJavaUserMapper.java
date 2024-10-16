package com.example.mapper;

import com.example.domain.StudyJavaUser;

import java.util.List;

/**
* @author opera
* @description 针对表【study_java_user】的数据库操作Mapper
* @createDate 2024-10-16 11:26:58
* @Entity com.example.domain.StudyJavaUser
*/
public interface StudyJavaUserMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StudyJavaUser record);

    int insertSelective(StudyJavaUser record);

    StudyJavaUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StudyJavaUser record);

    int updateByPrimaryKey(StudyJavaUser record);

    List<StudyJavaUser> selectAllUsers();
}
