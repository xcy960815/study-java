package com.example.service.impl;

import cn.hutool.json.JSONUtil;
import com.example.domain.dao.StudyJavaUserDao;
import com.example.domain.dto.StudyJavaLoginDto;
import com.example.domain.dto.StudyJavaUserDto;
import com.example.domain.vo.StudyJavaLoginVo;
import com.example.exception.StudyJavaException;
import com.example.domain.vo.StudyJavaUserVo;
import com.example.mapper.StudyJavaUserMapper;
import com.example.service.StudyJavaLoginService;
import com.example.service.StudyJavaUserService;
import com.example.component.JwtTokenComponent;
import com.example.component.RedisComponent;
import com.google.code.kaptcha.Producer;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Slf4j
@Service
public class StudyJavaLoginServiceImpl implements StudyJavaLoginService {

    /**
     * 验证码 key 名
     */
    private static final String CAPTCHA_KEY = "study-java-captcha";

    /**
     * 验证码过期时间
     */
    private static final int CAPTCHA_EXPIRE_TIME = 5;

    /**
     * token key 名
     */
    private static final String TOKEN_KEY = "study-java-token";

    /**
     * token 过期时间
     */
    private static final int TOKEN_EXPIRE_TIME = 24;

    @Resource
    private JwtTokenComponent jwtTokenComponent;

    @Resource
    private RedisComponent redisComponent;

    @Resource
    private Producer kaptchaProducer;

    @Resource
    private StudyJavaUserService studyJavaUserService;

    @Resource
    private StudyJavaUserMapper studyJavaUserMapper;

    @Override
    public StudyJavaLoginVo login(StudyJavaLoginDto studyJavaLoginParams)  {

        if (!redisComponent.hasKey(CAPTCHA_KEY)) {
            throw new StudyJavaException("验证码不存在");
        }

        String captcha =  redisComponent.get(CAPTCHA_KEY,String.class);

        if (!captcha.equalsIgnoreCase(studyJavaLoginParams.getCaptcha())) {

            throw new StudyJavaException("验证码错误");
        }
        StudyJavaUserDto studyJavaUserDto = new StudyJavaUserDto();

        studyJavaUserDto.setLoginName(studyJavaLoginParams.getUsername());

        StudyJavaUserDao userInfo = studyJavaUserMapper.getUserInfo(studyJavaUserDto);

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
        StudyJavaLoginVo studyJavaLoginVo = new StudyJavaLoginVo();
        studyJavaLoginVo.setUserId(userInfo.getUserId());
        studyJavaLoginVo.setLoginName(userInfo.getLoginName());
        studyJavaLoginVo.setAddress(userInfo.getAddress());
        studyJavaLoginVo.setCreateTime(userInfo.getCreateTime());
        studyJavaLoginVo.setIntroduceSign(userInfo.getIntroduceSign());
        studyJavaLoginVo.setNickName(userInfo.getNickName());
        Map<String,String> tokenContentOption = new HashMap<>();
        tokenContentOption.put("loginName",userInfo.getLoginName());
        tokenContentOption.put("userId",userInfo.getUserId().toString());
        // 将用户关键信息（能从数据库中查出来的字段保存进去）
        String tokenContent = JSONUtil.toJsonStr(tokenContentOption);
        String token = jwtTokenComponent.generateToken(tokenContent);
        redisComponent.setWithExpire(TOKEN_KEY, token,TOKEN_EXPIRE_TIME, TimeUnit.HOURS);
        studyJavaLoginVo.setToken(token);

        return studyJavaLoginVo;
    }


    public void logout(StudyJavaLoginDto studyJavaLoginParams) {
        // studyJavaUserService.logout(studyJavaLoginParams);
    }

    /**
     * 生成验证码
     * @return String
     * @throws IOException
     */
    public String getCaptcha() throws IOException {
        long startTime = System.currentTimeMillis(); // 获取开始时间(毫秒)
        // 生成验证码文本
        String captchaText = kaptchaProducer.createText();
        redisComponent.setWithExpire(CAPTCHA_KEY, captchaText,CAPTCHA_EXPIRE_TIME, TimeUnit.MINUTES);
        BufferedImage captchaImage = kaptchaProducer.createImage(captchaText);
        // 将验证码图像转换为 Base64
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(captchaImage, "jpg", byteArrayOutputStream);
        long endTime = System.currentTimeMillis(); // 获取结束时间(毫秒)
        // 毫秒转秒 并保留2位小数
        log.info("生成验证码耗时 {}",String.format("%.2f", (endTime - startTime) / 1000.0) + "秒");
        return  "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
    }
}
