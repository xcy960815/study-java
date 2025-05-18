package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.domain.dto.StudyJavaUserDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StudyJavaUserService {

    /**
    * 查询所有用户
    * @return IPage<StudyJavaUserDto>
    */
    IPage<StudyJavaUserDto> getUserList(Page<StudyJavaUserDto> page, StudyJavaUserDto userQueryData);

    /**
     * 更新用户
     */
    void updateUserInfo(StudyJavaUserDto studyJavaUser);

    String updateUserAvatar(String userId,MultipartFile file) throws IOException;
    /**
     * 添加用户
     */
    void insertUserInfo(StudyJavaUserDto studyJavaUser);
    /**
     * 删除用户
     */
    void deleteUserInfo(StudyJavaUserDto studyJavaUser);


    String generateBase64Image() throws IOException;

    /**
     * 通过token获取用户信息
     * @return StudyJavaUserDto
     */
    StudyJavaUserDto getUserInfo(StudyJavaUserDto studyJavaUserDto);

    void updateUserPassword(StudyJavaUserDto studyJavaUserDto);
}
