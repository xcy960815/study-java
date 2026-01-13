package com.studyjava.service.impl;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.studyjava.component.JwtTokenComponent;
import com.studyjava.domain.dao.StudyJavaSysUserDao;
import com.studyjava.domain.dto.StudyJavaLoginDto;
import com.studyjava.domain.dto.StudyJavaSysUserDto;
import com.studyjava.domain.vo.StudyJavaSysUserVo;
import com.studyjava.exception.StudyJavaException;
import com.studyjava.mapper.StudyJavaSysUserMapper;
import com.studyjava.service.StudyJavaSysUserService;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StudyJavaSysUserServiceImpl implements StudyJavaSysUserService {

  @Resource private StudyJavaSysUserMapper studyJavaSysUserMapper;

  @Resource private JwtTokenComponent jwtTokenComponent;

  @Resource private com.studyjava.mapper.StudyJavaSysMenuMapper studyJavaSysMenuMapper;

  /** Dto转Dao */
  private StudyJavaSysUserDao makeDto2Dao(StudyJavaSysUserDto studyJavaSysUserDto) {
    StudyJavaSysUserDao studyJavaSysUserDao = new StudyJavaSysUserDao();
    BeanUtils.copyProperties(studyJavaSysUserDto, studyJavaSysUserDao);
    // 手动映射userId到id
    if (studyJavaSysUserDto.getId() != null) {
      studyJavaSysUserDao.setId(studyJavaSysUserDto.getId());
    }
    // 新增：拷贝roleIds
    studyJavaSysUserDao.setRoleIds(studyJavaSysUserDto.getRoleIds());
    return studyJavaSysUserDao;
  }

  /** Dao转Vo */
  private StudyJavaSysUserVo makeDao2Vo(StudyJavaSysUserDao studyJavaSysUserDao) {
    StudyJavaSysUserVo studyJavaSysUserVo = new StudyJavaSysUserVo();
    BeanUtils.copyProperties(studyJavaSysUserDao, studyJavaSysUserVo);
    return studyJavaSysUserVo;
  }

  /** 查询所有用户 */
  @Override
  public IPage<StudyJavaSysUserVo> getUserList(
      IPage<StudyJavaSysUserDao> page, StudyJavaSysUserDto studyJavaSysUserDto) {
    IPage<StudyJavaSysUserDao> daoPage =
        studyJavaSysUserMapper.getUserList(page, makeDto2Dao(studyJavaSysUserDto));
    List<StudyJavaSysUserVo> voList =
        daoPage.getRecords().stream().map(this::convertToVo).collect(Collectors.toList());
    IPage<StudyJavaSysUserVo> voPage =
        new Page<>(daoPage.getCurrent(), daoPage.getSize(), daoPage.getTotal());
    voPage.setRecords(voList);
    return voPage;
  }

  // Dao转Vo方法
  private StudyJavaSysUserVo convertToVo(StudyJavaSysUserDao dao) {
    if (dao == null) return null;
    StudyJavaSysUserVo vo = new StudyJavaSysUserVo();
    BeanUtils.copyProperties(dao, vo);
    return vo;
  }

  @Override
  @Transactional
  public Boolean updateUser(StudyJavaSysUserDto studyJavaSysUserDto) {
    // 1. 更新用户主表
    boolean userUpdate = studyJavaSysUserMapper.updateUser(makeDto2Dao(studyJavaSysUserDto)) > 0;

    // 2. 删除旧的角色关系
    studyJavaSysUserMapper.deleteUserRolesByUserId(studyJavaSysUserDto.getId());

    // 3. 批量插入新的角色关系
    if (studyJavaSysUserDto.getRoleIds() != null && !studyJavaSysUserDto.getRoleIds().isEmpty()) {
      studyJavaSysUserMapper.insertUserRolesBatch(
          studyJavaSysUserDto.getId(), studyJavaSysUserDto.getRoleIds());
    }
    return userUpdate;
  }

  @Override
  public String updateUserAvatar(@NonNull String userId, @NonNull MultipartFile file)
      throws IOException {
    //  将文件转成base64
    if (file.isEmpty()) {
      throw new StudyJavaException("上传的文件为空");
    }
    // 将文件转成字节数组
    byte[] fileBytes = file.getBytes();

    // 将字节数组转成 Base64 编码的字符串
    String base64Image = Base64.getEncoder().encodeToString(fileBytes);
    String base64ImageUrl = "data:image/jpeg;base64," + base64Image;
    studyJavaSysUserMapper.updateUserAvatar(userId, base64ImageUrl);

    return base64ImageUrl;
  }

  /** 创建一条数据 */
  @Override
  public Boolean insertUser(StudyJavaSysUserDto studyJavaSysUserDto) {
    StudyJavaSysUserDao studyJavaSysUserDao = makeDto2Dao(studyJavaSysUserDto);
    studyJavaSysUserDao.setLockedFlag(0);
    return studyJavaSysUserMapper.insertUser(studyJavaSysUserDao) > 0;
  }

  /** 删除用户 */
  @Override
  @Transactional
  public Boolean deleteUser(StudyJavaSysUserDto studyJavaSysUserDto) {
    StudyJavaSysUserDao studyJavaSysUserDao = makeDto2Dao(studyJavaSysUserDto);

    // 删除用户角色关联
    studyJavaSysUserMapper.deleteUserRolesByUserId(studyJavaSysUserDto.getId());
    return studyJavaSysUserMapper.deleteUser(studyJavaSysUserDao) > 0;
  }

  // 在 Service 实现类中抛出 IOException
  @Override
  public String generateBase64Image() throws IOException {
    // 创建一个空白的 BufferedImage
    int width = 200;
    int height = 100;
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics2D graphics = image.createGraphics();

    // 填充背景色
    graphics.setColor(Color.BLUE);
    graphics.fillRect(0, 0, width, height);

    // 画一个简单的矩形
    graphics.setColor(Color.BLUE);
    graphics.fillRect(50, 25, 100, 50);

    // 结束绘制
    graphics.dispose();

    // 将图片转换为 Base64 字符串
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    ImageIO.write(image, "png", byteArrayOutputStream);
    byte[] imageBytes = byteArrayOutputStream.toByteArray();

    // 编码为 Base64
    return Base64.getEncoder().encodeToString(imageBytes);
  }

  @Override
  public StudyJavaSysUserVo getUserInfo() {
    ServletRequestAttributes attributes =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    if (attributes == null) {
      return null;
    }
    HttpServletRequest request = attributes.getRequest();
    String authorization = request.getHeader("Authorization");
    JSONObject tokenUserInfo =
        JSONUtil.parseObj(jwtTokenComponent.getUserInfoFromAuthorization(authorization));
    Long userId = Long.parseLong(tokenUserInfo.get("userId").toString());
    String loginName = tokenUserInfo.get("loginName").toString();
    StudyJavaSysUserDto studyJavaSysUserDto = new StudyJavaSysUserDto();
    studyJavaSysUserDto.setId(userId);
    studyJavaSysUserDto.setLoginName(loginName);
    StudyJavaSysUserVo vo =
        makeDao2Vo(studyJavaSysUserMapper.getUserInfo(makeDto2Dao(studyJavaSysUserDto)));
    if (vo != null) {
      // 如果是超级管理员，赋予所有权限标识
      if (vo.getRoleCodes() != null && vo.getRoleCodes().contains("SUPER_ADMIN")) {
        vo.setPermissions(List.of("*:*:*"));
      } else {
        vo.setPermissions(studyJavaSysMenuMapper.getPermsByUserId(vo.getId()));
      }
    }
    return vo;
  }

  @Override
  public StudyJavaSysUserVo getUserInfo(StudyJavaLoginDto studyJavaLoginDto) {
    String loginName = studyJavaLoginDto.getUsername();
    //        String passwordMd5 = studyJavaLoginDto.getPasswordMd5();
    StudyJavaSysUserDto studyJavaSysUserDto = new StudyJavaSysUserDto();
    studyJavaSysUserDto.setLoginName(loginName);
    StudyJavaSysUserVo vo =
        makeDao2Vo(studyJavaSysUserMapper.getUserInfo(makeDto2Dao(studyJavaSysUserDto)));
    if (vo != null) {
      // 如果是超级管理员，赋予所有权限标识
      if (vo.getRoleCodes() != null && vo.getRoleCodes().contains("SUPER_ADMIN")) {
        vo.setPermissions(List.of("*:*:*"));
      } else {
        vo.setPermissions(studyJavaSysMenuMapper.getPermsByUserId(vo.getId()));
      }
    }
    return vo;
  }

  /**
   * 更新用户密码
   *
   * @param studyJavaSysUserDto StudyJavaSysUserDto
   */
  @Override
  public Boolean updateUserPassword(StudyJavaSysUserDto studyJavaSysUserDto) {
    StudyJavaSysUserVo studyJavaUserDao = this.getUserInfo();
    String passwordMd5 = studyJavaUserDao.getPasswordMd5();
    String newPasswordMd5 = studyJavaSysUserDto.getNewPasswordMd5();
    String confirmNewPasswordMd5 = studyJavaSysUserDto.getConfirmNewPasswordMd5();
    if (!newPasswordMd5.equals(confirmNewPasswordMd5)) {
      throw new StudyJavaException("两次密码不一致");
    }
    if (!newPasswordMd5.equals(passwordMd5)) {
      throw new StudyJavaException("原密码不正确");
    }
    studyJavaSysUserDto.setPasswordMd5(newPasswordMd5);

    return studyJavaSysUserMapper.updateUser(makeDto2Dao(studyJavaSysUserDto)) > 0;
  }
}
