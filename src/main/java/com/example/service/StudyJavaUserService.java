package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.domain.dto.StudyJavaUserDto;
import com.example.domain.vo.StudyJavaLoginVo;
import com.example.domain.vo.StudyJavaUserVo;

public interface StudyJavaUserService {

    /**
    * 查询所有用户
    * @return IPage<StudyJavaUser>
    */
    IPage<StudyJavaUserDto> getUserList(Page<StudyJavaUserVo> page, StudyJavaUserVo userQueryData);

    /**
     * 更新用户
     */
    int updateUser(StudyJavaUserVo studyJavaUser);

    /**
     * 添加用户
     */
    int insertUser(StudyJavaUserVo studyJavaUser);
    /**
     * 删除用户
     */
    int deleteUser(StudyJavaUserVo studyJavaUser);

    /**
     *
     * @param studyJavaLoginParams
     * @return
     */
    StudyJavaUserDto getUserByNameAndPassword(StudyJavaLoginVo studyJavaLoginParams);
}
