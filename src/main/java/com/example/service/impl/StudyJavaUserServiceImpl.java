package com.example.service.imp;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.domain.dao.StudyJavaUserDao;
import com.example.domain.dto.StudyJavaUserDto;
import com.example.domain.vo.StudyJavaUserVo;
import com.example.exception.StudyJavaException;
import com.example.mapper.StudyJavaUserMapper;
import com.example.service.StudyJavaUserService;
import jakarta.annotation.Resource;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.stream.Collectors;


@Slf4j
@Service
public class StudyJavaUserServiceImpl implements StudyJavaUserService {

    @Resource
    private StudyJavaUserMapper studyJavaUserMapper;

    /**
     * 查询所有用户
     */
    @Override
    public IPage<StudyJavaUserDto> getUserList(Page<StudyJavaUserVo> page ,StudyJavaUserVo userQueryData) {
        IPage<StudyJavaUserDao> userPageResult = studyJavaUserMapper.getUserList(page,userQueryData);
        List<StudyJavaUserDto> userList = userPageResult.getRecords().stream().map(this::makeDaoToDto).collect(Collectors.toList());
        // 创建新的 IPage 对象
        IPage<StudyJavaUserDto> resultPage = new Page<>(userPageResult.getCurrent(), userPageResult.getSize(), userPageResult.getTotal());
        resultPage.setRecords(userList);

        return resultPage;
    }

    /**
     * dao 2 dto
     * @param userInfoDao StudyJavaUserDao
     * @return StudyJavaUserDto
     */
    private StudyJavaUserDto makeDaoToDto(StudyJavaUserDao userInfoDao) {
        StudyJavaUserDto studyJavaUserDto = new StudyJavaUserDto();
        studyJavaUserDto.setUserId(userInfoDao.getUserId());
        studyJavaUserDto.setAddress(userInfoDao.getAddress());
        studyJavaUserDto.setNickName(userInfoDao.getNickName());
        studyJavaUserDto.setLoginName(userInfoDao.getLoginName());
        studyJavaUserDto.setIntroduceSign(userInfoDao.getIntroduceSign());
        studyJavaUserDto.setCreateTime(userInfoDao.getCreateTime());
        // 年龄 随机生成
        Random random = new Random();
        Integer age = random.nextInt(100);
        studyJavaUserDto.setAge(age);
        try {
            if(userInfoDao.getAvatar() != null) {
                studyJavaUserDto.setAvatar(userInfoDao.getAvatar());
            } else {
                // 设置默认头像
                studyJavaUserDto.setAvatar(generateBase64Image());
            }
        } catch (IOException e) {
            log.error("设置默认头像失败 {}",e.getMessage());
           throw new StudyJavaException("设置默认头像失败" + e.getMessage());
        }
        return studyJavaUserDto;
    };
    @Override
    public void updateUserInfo(StudyJavaUserVo studyJavaUser) {
        studyJavaUserMapper.updateUserInfo(studyJavaUser);
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
        studyJavaUserMapper.updateUserAvatar(userId, base64ImageUrl);

        return base64ImageUrl;
    };

    /**
     * 创建一条数据
     *
     * @param studyJavaUser StudyJavaUserVo
     */
    @Override
    public void insertUserInfo(StudyJavaUserVo studyJavaUser) {
        studyJavaUser.setIsDeleted(0);
        studyJavaUser.setLockedFlag(0);
        Date createTime = new Date();
        studyJavaUser.setCreateTime(createTime);
        studyJavaUserMapper.insertUserInfo(studyJavaUser);
    }

    /**
     * 删除用户
     *
     * @param studyJavaUser StudyJavaUserVo
     */
    @Override
    public void deleteUserInfo(StudyJavaUserVo studyJavaUser) {
        studyJavaUserMapper.deleteUserInfo(studyJavaUser);
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
    public StudyJavaUserDto getUserInfo(StudyJavaUserVo studyJavaUserVo){
        StudyJavaUserDao userInfoDao = studyJavaUserMapper.getUserInfo(studyJavaUserVo);
        return makeDaoToDto(userInfoDao);
    }

    /**
     * 更新用户密码
     * @param studyJavaUserVo StudyJavaUserVo
     */
    @Override
    public void updateUserPassword(StudyJavaUserVo studyJavaUserVo) {
        StudyJavaUserDao studyJavaUserDao = studyJavaUserMapper.getUserInfo(studyJavaUserVo);
        String passwordMd5 = studyJavaUserDao.getPasswordMd5();
        String newPasswordMd5 = studyJavaUserVo.getNewPasswordMd5();
        String confirmNewPasswordMd5 = studyJavaUserVo.getConfirmNewPasswordMd5();
        if (!newPasswordMd5.equals(confirmNewPasswordMd5)) {
            throw new StudyJavaException("两次密码不一致");
        }
        if (!newPasswordMd5.equals(passwordMd5)) {
            throw new StudyJavaException("原密码不正确");
        }

        studyJavaUserVo.setPasswordMd5(newPasswordMd5);

        studyJavaUserMapper.updateUserInfo(studyJavaUserVo);
    }
}
