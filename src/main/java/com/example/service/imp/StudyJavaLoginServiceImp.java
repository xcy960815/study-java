package com.example.service.imp;


import com.example.domain.dao.StudyJavaUserDao;
import com.example.domain.dto.StudyJavaLoginDto;
import com.example.domain.vo.StudyJavaLoginVo;
import com.example.exception.StudyJavaException;
import com.example.domain.vo.StudyJavaUserVo;
import com.example.mapper.StudyJavaUserMapper;
import com.example.service.StudyJavaLoginService;
import com.example.service.StudyJavaUserService;
import com.example.utils.JwtTokenUtil;
import com.example.utils.RedisUtil;
import com.google.code.kaptcha.Producer;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class StudyJavaLoginServiceImp implements StudyJavaLoginService {
    // 声明一个静态的固定值
    private final String CAPTCHA_KEY = "captcha";

    @Autowired
    private Producer kaptchaProducer;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    private StudyJavaUserService studyJavaUserService;

    @Resource
    private StudyJavaUserMapper studyJavaUserMapper;

    @Override
    public StudyJavaLoginDto login(StudyJavaLoginVo studyJavaLoginParams)  {
        // 账号密码校验
        if(StringUtils.isBlank(studyJavaLoginParams.getUsername()) ) {
            throw new StudyJavaException("用户名不能为空");
        }
        if(StringUtils.isBlank(studyJavaLoginParams.getPassword())){
            throw new StudyJavaException("密码不能为空");
        }
        StudyJavaUserVo studyJavaUserVo = new StudyJavaUserVo();

        studyJavaUserVo.setLoginName(studyJavaLoginParams.getUsername());

        StudyJavaUserDao userInfo = studyJavaUserMapper.getUserInfo(studyJavaUserVo);

        if (userInfo == null) {
            throw new StudyJavaException("用户不存在");
        }

        if(!StringUtils.isBlank(userInfo.getPasswordMd5()) && !StringUtils.isBlank(studyJavaLoginParams.getPassword())) {
            String dataBasePassword = userInfo.getPasswordMd5();
            String loginPassword = studyJavaLoginParams.getPassword();
            if(!dataBasePassword.equals(loginPassword)){
                throw new StudyJavaException("密码错误");
            }
        }
        StudyJavaLoginDto studyJavaLoginDto = new StudyJavaLoginDto();
        studyJavaLoginDto.setUserId(userInfo.getUserId());
        studyJavaLoginDto.setLoginName(userInfo.getLoginName());
        studyJavaLoginDto.setAddress(userInfo.getAddress());
        studyJavaLoginDto.setCreateTime(userInfo.getCreateTime());
        studyJavaLoginDto.setIntroduceSign(userInfo.getIntroduceSign());
        studyJavaLoginDto.setNickName(userInfo.getNickName());
        // 将用户关键信息（能从数据库中查出来的字段保存进去）
        String tokenContent = userInfo.getUserId() + ":" + userInfo.getLoginName() + ":" + userInfo.getNickName();
        studyJavaLoginDto.setToken(jwtTokenUtil.generateToken(tokenContent));
        return studyJavaLoginDto;
    }


    public void logout(StudyJavaLoginVo studyJavaLoginParams) {
        // studyJavaUserService.logout(studyJavaLoginParams);
    }

    public String getCaptcha() throws IOException {
        // 生成验证码文本
        String captchaText = kaptchaProducer.createText();
        RedisUtil.setWithExpire(CAPTCHA_KEY, captchaText,10, TimeUnit.SECONDS);
        BufferedImage captchaImage = kaptchaProducer.createImage(captchaText);

        // 将验证码图像转换为 Base64
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(captchaImage, "jpg", byteArrayOutputStream);
        String base64Image = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
        log.info("base64Image{}", base64Image);
        // 返回 Base64 字符串
        return base64Image;
    }
}
