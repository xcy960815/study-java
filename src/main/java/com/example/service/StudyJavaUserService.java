package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.domain.StudyJavaUser;



public interface StudyJavaUserService {

    /**
    * 查询所有用户
    * @return IPage<StudyJavaUser>
    */
    IPage<StudyJavaUser> getUserList(Page<StudyJavaUser> page, StudyJavaUser userQueryData);

    /**
     * 更新用户
     */
    int updateUser(StudyJavaUser studyJavaUser);

    /**
     * 添加用户
     */
    int insertUser(StudyJavaUser studyJavaUser);
    /**
     * 删除用户
     */
    int deleteUser(StudyJavaUser studyJavaUser);



}
