package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.domain.dto.StudyJavaUserDto;
import com.example.domain.vo.StudyJavaUserVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StudyJavaUserService {

    /**
    * 查询所有用户
    * @return IPage<StudyJavaUser>
    */
    IPage<StudyJavaUserDto> getUserList(Page<StudyJavaUserVo> page, StudyJavaUserVo userQueryData);

    /**
     * 更新用户
     */
    void updateUserInfo(StudyJavaUserVo studyJavaUser);

    String updateUserAvatar(String userId,MultipartFile file) throws IOException;
    /**
     * 添加用户
     */
    void insertUserInfo(StudyJavaUserVo studyJavaUser);
    /**
     * 删除用户
     */
    void deleteUserInfo(StudyJavaUserVo studyJavaUser);


    String generateBase64Image() throws IOException;

    /**
     * 通过token获取用户信息
     * @return StudyJavaUserDto
     */
    StudyJavaUserDto getUserInfo(StudyJavaUserVo studyJavaUserVo);

    void updateUserPassword(StudyJavaUserVo studyJavaUserVo);
}
