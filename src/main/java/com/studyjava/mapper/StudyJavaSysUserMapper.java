package com.studyjava.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.studyjava.domain.dao.StudyJavaSysUserDao;

/**
 * @author opera 针对表【study_java_sys_user】的数据库操作Mapper 2024-10-16 11:26:58
 *     com.example.domain.StudyJavaUser
 */
@Repository
public interface StudyJavaSysUserMapper extends BaseMapper<StudyJavaSysUserDao> {
  // 获取用户列表
  IPage<StudyJavaSysUserDao> getUserList(
      @Param("page") IPage<StudyJavaSysUserDao> page,
      @Param("studyJavaSysUserDao") StudyJavaSysUserDao studyJavaSysUserDao);

  // 更新用户
  int updateUser(@Param("studyJavaSysUserDao") StudyJavaSysUserDao studyJavaSysUserDao);

  // 添加用户
  int insertUser(@Param("studyJavaSysUserDao") StudyJavaSysUserDao studyJavaSysUserDao);

  // 删除用户
  int deleteUser(@Param("studyJavaSysUserDao") StudyJavaSysUserDao studyJavaSysUserDao);

  // 更新用户头像
  int updateUserAvatar(
      @Param("userId") String userId, @Param("base64ImageUrl") String base64ImageUrl);

  // 查找用户信息
  StudyJavaSysUserDao getUserInfo(
      @Param("studyJavaSysUserDao") StudyJavaSysUserDao studyJavaSysUserDao);

  // 删除用户所有角色
  void deleteUserRolesByUserId(@Param("userId") Long userId);

  // 批量插入用户角色
  void insertUserRolesBatch(
      @Param("userId") Long userId, @Param("roleIds") java.util.List<Long> roleIds);
}
