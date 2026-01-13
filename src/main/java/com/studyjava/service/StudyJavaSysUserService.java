package com.studyjava.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.studyjava.domain.dao.StudyJavaSysUserDao;
import com.studyjava.domain.dto.StudyJavaLoginDto;
import com.studyjava.domain.dto.StudyJavaSysUserDto;
import com.studyjava.domain.vo.StudyJavaSysUserVo;

public interface StudyJavaSysUserService {

  /**
   * 查询所有用户
   *
   * @return IPage<StudyJavaSysUserDto>
   */
  //    IPage<StudyJavaSysUserVo> getUserList(Page<StudyJavaSysUserDao> page, StudyJavaSysUserDto
  // userQueryData);
  IPage<StudyJavaSysUserVo> getUserList(
      IPage<StudyJavaSysUserDao> page, StudyJavaSysUserDto studyJavaSysUserDto);

  /** 更新用户 */
  Boolean updateUser(StudyJavaSysUserDto studyJavaSysUser);

  String updateUserAvatar(String userId, MultipartFile file) throws IOException;

  /** 添加用户 */
  Boolean insertUser(StudyJavaSysUserDto studyJavaUser);

  /** 删除用户 */
  Boolean deleteUser(StudyJavaSysUserDto studyJavaSysUserDto);

  String generateBase64Image() throws IOException;

  /**
   * 通过token获取用户信息
   *
   * @return StudyJavaSysUserDto
   */
  StudyJavaSysUserVo getUserInfo();

  StudyJavaSysUserVo getUserInfo(StudyJavaLoginDto studyJavaLoginDto);

  Boolean updateUserPassword(StudyJavaSysUserDto studyJavaSysUserDto);
}
