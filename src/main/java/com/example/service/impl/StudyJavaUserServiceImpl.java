package com.example.service.impl;




import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.component.JwtTokenComponent;
import com.example.domain.dao.StudyJavaUserDao;
import com.example.domain.dto.StudyJavaLoginDto;
import com.example.domain.dto.StudyJavaUserDto;
import com.example.domain.vo.StudyJavaUserVo;
import com.example.exception.StudyJavaException;
import com.example.mapper.StudyJavaUserMapper;
import com.example.service.StudyJavaUserService;
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

    @Resource
    private JwtTokenComponent jwtTokenComponent;

    private StudyJavaUserDao makeDto2Dao(StudyJavaUserDto studyJavaUserDto){
        StudyJavaUserDao studyJavaUserDao = new StudyJavaUserDao();
        BeanUtils.copyProperties(studyJavaUserDto, studyJavaUserDao);
        return studyJavaUserDao;
    }

//    private StudyJavaUserVo makeDaoToVo(StudyJavaUserDao userInfoDao) {
//        StudyJavaUserVo studyJavaUserVo = new StudyJavaUserVo();
//        BeanUtils.copyProperties(userInfoDao, studyJavaUserVo);
//        return studyJavaUserVo;
//    }
    /**
     * 查询所有用户
     */
    @Override
    public IPage<StudyJavaUserVo> getUserList(Page<StudyJavaUserDto> page, StudyJavaUserDto studyJavaUserDto) {
        IPage<StudyJavaUserDao> userDaoResult = studyJavaUserMapper.getUserList(page,makeDto2Dao(studyJavaUserDto));
        List<StudyJavaUserVo> userVoList = userDaoResult.getRecords().stream().map(this::makeDaoToVo).collect(Collectors.toList());
        // 创建新的 IPage 对象
        IPage<StudyJavaUserVo> resultPage = new Page<>(userDaoResult.getCurrent(), userDaoResult.getSize(), userDaoResult.getTotal());
        resultPage.setRecords(userVoList);
        return resultPage;
    }

    /**
     * dao 2 vo
     * @param userInfoDao StudyJavaUserDao
     * @return StudyJavaUserDto
     */
    private StudyJavaUserVo makeDaoToVo(StudyJavaUserDao userInfoDao) {
        StudyJavaUserVo studyJavaUserVo = new StudyJavaUserVo();
        studyJavaUserVo.setUserId(userInfoDao.getUserId());
        studyJavaUserVo.setAddress(userInfoDao.getAddress());
        studyJavaUserVo.setNickName(userInfoDao.getNickName());
        studyJavaUserVo.setLoginName(userInfoDao.getLoginName());
        studyJavaUserVo.setIntroduceSign(userInfoDao.getIntroduceSign());
        studyJavaUserVo.setCreateTime(userInfoDao.getCreateTime());
        // 年龄 随机生成
        Random random = new Random();
        Integer age = random.nextInt(100);
        studyJavaUserVo.setAge(age);
        try {
            if(userInfoDao.getAvatar() != null) {
                studyJavaUserVo.setAvatar(userInfoDao.getAvatar());
            } else {
                // 设置默认头像
                studyJavaUserVo.setAvatar(generateBase64Image());
            }
        } catch (IOException e) {
            log.error("设置默认头像失败 {}",e.getMessage());
           throw new StudyJavaException("设置默认头像失败" + e.getMessage());
        }
        return studyJavaUserVo;
    }


    @Override
    public Boolean updateUserInfo(StudyJavaUserDto studyJavaUserDto) {
       return studyJavaUserMapper.updateUserInfo(makeDto2Dao(studyJavaUserDto)) > 0;
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
    }

    /**
     * 创建一条数据
     */
    @Override
    public Boolean insertUserInfo(StudyJavaUserDto studyJavaUserDto) {
        StudyJavaUserDao studyJavaUserDao = makeDto2Dao(studyJavaUserDto);
        studyJavaUserDao.setIsDeleted(0);
        studyJavaUserDao.setLockedFlag(0);
        studyJavaUserDao.setCreateTime(new Date());
        return studyJavaUserMapper.insertUserInfo(studyJavaUserDao) > 0;
    }

    /**
     * 删除用户
     */
    @Override
    public Boolean deleteUserInfo(StudyJavaUserDto studyJavaUserDto) {
        StudyJavaUserDao studyJavaUserDao = makeDto2Dao(studyJavaUserDto);
        return studyJavaUserMapper.deleteUserInfo(studyJavaUserDao) > 0;
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
    public StudyJavaUserVo getUserInfo(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }
        HttpServletRequest request = attributes.getRequest();
        String authorization =  request.getHeader("Authorization");
        JSONObject tokenUserInfo = JSONUtil.parseObj(jwtTokenComponent.getUserInfoFromAuthorization(authorization));
        Long userId = Long.parseLong(tokenUserInfo.get("userId").toString());
        String loginName = tokenUserInfo.get("loginName").toString();
        StudyJavaUserDto studyJavaUserDto = new StudyJavaUserDto();
        studyJavaUserDto.setUserId(userId);
        studyJavaUserDto.setLoginName(loginName);
        StudyJavaUserDao userInfoDao = studyJavaUserMapper.getUserInfo(makeDto2Dao(studyJavaUserDto));
        return makeDaoToVo(userInfoDao);
    }

    @Override
    public StudyJavaUserVo getUserInfo(StudyJavaLoginDto studyJavaLoginDto){
        String loginName = studyJavaLoginDto.getUsername();
//        String passwordMd5 = studyJavaLoginDto.getPasswordMd5();
        StudyJavaUserDto studyJavaUserDto = new StudyJavaUserDto();
//        studyJavaUserDto.setPasswordMd5(passwordMd5);
        studyJavaUserDto.setLoginName(loginName);
        StudyJavaUserDao userInfoDao = studyJavaUserMapper.getUserInfo(makeDto2Dao(studyJavaUserDto));
        return makeDaoToVo(userInfoDao);
    }

    /**
     * 更新用户密码
     * @param studyJavaUserDto StudyJavaUserDto
     */
    @Override
    public Boolean updateUserPassword(StudyJavaUserDto studyJavaUserDto) {
        StudyJavaUserVo studyJavaUserDao = this.getUserInfo();
        String passwordMd5 = studyJavaUserDao.getPasswordMd5();
        String newPasswordMd5 = studyJavaUserDto.getNewPasswordMd5();
        String confirmNewPasswordMd5 = studyJavaUserDto.getConfirmNewPasswordMd5();
        if (!newPasswordMd5.equals(confirmNewPasswordMd5)) {
            throw new StudyJavaException("两次密码不一致");
        }
        if (!newPasswordMd5.equals(passwordMd5)) {
            throw new StudyJavaException("原密码不正确");
        }
        studyJavaUserDto.setPasswordMd5(newPasswordMd5);

     return studyJavaUserMapper.updateUserInfo(makeDto2Dao(studyJavaUserDto)) > 0;
    }
}
