package com.example.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.component.JwtTokenComponent;
import com.example.domain.dao.StudyJavaSysUserDao;
import com.example.domain.dto.StudyJavaLoginDto;
import com.example.domain.dto.StudyJavaSysUserDto;
import com.example.domain.vo.StudyJavaSysUserVo;
import com.example.exception.StudyJavaException;
import com.example.mapper.StudyJavaSysUserMapper;
import com.example.service.StudyJavaSysUserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import java.util.Date;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
public class StudyJavaSysUserServiceImpl implements StudyJavaSysUserService {

    @Resource
    private StudyJavaSysUserMapper studyJavaSysUserMapper;

    @Resource
    private JwtTokenComponent jwtTokenComponent;

    private StudyJavaSysUserDao makeDto2Dao(StudyJavaSysUserDto studyJavaSysUserDto){
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

    /**
     * 查询所有用户
     */
    @Override
    public IPage<StudyJavaSysUserVo> getUserList(Page<StudyJavaSysUserDto> page, StudyJavaSysUserDto studyJavaSysUserDto) {
        return studyJavaSysUserMapper.getUserList(page, makeDto2Dao(studyJavaSysUserDto));
    }

    @Override
    @Transactional
    public Boolean updateUserInfo(StudyJavaSysUserDto studyJavaSysUserDto) {
        // 1. 更新用户主表
        boolean userUpdate = studyJavaSysUserMapper.updateUserInfo(makeDto2Dao(studyJavaSysUserDto)) > 0;

        // 2. 删除旧的角色关系
        studyJavaSysUserMapper.deleteUserRolesByUserId(studyJavaSysUserDto.getId());

        // 3. 批量插入新的角色关系
        if (studyJavaSysUserDto.getRoleIds() != null && !studyJavaSysUserDto.getRoleIds().isEmpty()) {
            studyJavaSysUserMapper.insertUserRolesBatch(studyJavaSysUserDto.getId(), studyJavaSysUserDto.getRoleIds());
        }
        return userUpdate;
    }

    @Override
    public String updateUserAvatar(@NonNull String userId, @NonNull MultipartFile file) throws IOException {
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

    /**
     * 创建一条数据
     */
    @Override
    public Boolean insertUserInfo(StudyJavaSysUserDto studyJavaSysUserDto) {
        StudyJavaSysUserDao studyJavaSysUserDao = makeDto2Dao(studyJavaSysUserDto);
        studyJavaSysUserDao.setIsDeleted(0);
        studyJavaSysUserDao.setLockedFlag(0);
        studyJavaSysUserDao.setCreateTime(new Date());
        return studyJavaSysUserMapper.insertUserInfo(studyJavaSysUserDao) > 0;
    }

    /**
     * 删除用户
     */
    @Override
    public Boolean deleteUserInfo(StudyJavaSysUserDto studyJavaSysUserDto) {
        StudyJavaSysUserDao studyJavaSysUserDao = makeDto2Dao(studyJavaSysUserDto);
        return studyJavaSysUserMapper.deleteUserInfo(studyJavaSysUserDao) > 0;
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
    public StudyJavaSysUserVo getUserInfo(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }
        HttpServletRequest request = attributes.getRequest();
        String authorization =  request.getHeader("Authorization");
        JSONObject tokenUserInfo = JSONUtil.parseObj(jwtTokenComponent.getUserInfoFromAuthorization(authorization));
        Long userId = Long.parseLong(tokenUserInfo.get("userId").toString());
        String loginName = tokenUserInfo.get("loginName").toString();
        StudyJavaSysUserDto studyJavaSysUserDto = new StudyJavaSysUserDto();
        studyJavaSysUserDto.setId(userId);
        studyJavaSysUserDto.setLoginName(loginName);
       return studyJavaSysUserMapper.getUserInfo(makeDto2Dao(studyJavaSysUserDto));
    }

    @Override
    public StudyJavaSysUserVo getUserInfo(StudyJavaLoginDto studyJavaLoginDto){
        String loginName = studyJavaLoginDto.getUsername();
//        String passwordMd5 = studyJavaLoginDto.getPasswordMd5();
        StudyJavaSysUserDto studyJavaSysUserDto = new StudyJavaSysUserDto();
//        studyJavaSysUserDto.setPasswordMd5(passwordMd5);
        studyJavaSysUserDto.setLoginName(loginName);
        return studyJavaSysUserMapper.getUserInfo(makeDto2Dao(studyJavaSysUserDto));
    }

    /**
     * 更新用户密码
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

     return studyJavaSysUserMapper.updateUserInfo(makeDto2Dao(studyJavaSysUserDto)) > 0;
    }
}
