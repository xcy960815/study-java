package com.studyjava.service.impl;

import cn.hutool.json.JSONUtil;
import com.studyjava.domain.dto.StudyJavaLoginDto;
import com.studyjava.domain.dto.StudyJavaSysUserDto;
import com.studyjava.domain.vo.StudyJavaSysLoginVo;
import com.studyjava.domain.vo.StudyJavaSysUserVo;
import com.studyjava.exception.StudyJavaException;
import com.studyjava.service.StudyJavaLoginService;
import com.studyjava.service.StudyJavaSysUserService;
import com.studyjava.component.JwtTokenComponent;
import com.studyjava.component.RedisComponent;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpServletRequest;


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
    private StudyJavaSysUserService studyJavaSysUserService;

//    @Resource
//    private StudyJavaSysUserMapper studyJavaUserMapper;

    @Override
    public StudyJavaSysLoginVo login(StudyJavaLoginDto studyJavaLoginDto)  {

        if (!redisComponent.hasKey(CAPTCHA_KEY)) {
            throw new StudyJavaException("验证码不存在");
        }

        String captcha =  redisComponent.get(CAPTCHA_KEY,String.class);

        if (!captcha.equalsIgnoreCase(studyJavaLoginDto.getCaptcha())) {

            throw new StudyJavaException("验证码错误");
        }
        StudyJavaSysUserDto studyJavaSysUserDto = new StudyJavaSysUserDto();

        studyJavaSysUserDto.setLoginName(studyJavaLoginDto.getUsername());



        StudyJavaSysUserVo userInfoVo = studyJavaSysUserService.getUserInfo(studyJavaLoginDto);

        if (userInfoVo == null) {
            throw new StudyJavaException("用户不存在");
        }

        if(!StringUtils.isBlank(userInfoVo.getPasswordMd5()) && !StringUtils.isBlank(studyJavaLoginDto.getPassword())) {
            String dataBasePassword = userInfoVo.getPasswordMd5();
            String loginPassword = studyJavaLoginDto.getPassword();
            if(!dataBasePassword.equals(loginPassword)){
                throw new StudyJavaException("密码错误");
            }
        }
        StudyJavaSysLoginVo studyJavaLoginVo = new StudyJavaSysLoginVo();
        studyJavaLoginVo.setId(userInfoVo.getId());
        studyJavaLoginVo.setLoginName(userInfoVo.getLoginName());
        studyJavaLoginVo.setAddress(userInfoVo.getAddress());
        studyJavaLoginVo.setCreateTime(userInfoVo.getCreateTime());
        studyJavaLoginVo.setIntroduceSign(userInfoVo.getIntroduceSign());
        studyJavaLoginVo.setNickName(userInfoVo.getNickName());
        Map<String,String> tokenContentOption = new HashMap<>();
        tokenContentOption.put("loginName",userInfoVo.getLoginName());
        tokenContentOption.put("userId",userInfoVo.getId().toString());
        // 将用户关键信息（能从数据库中查出来的字段保存进去）
        String tokenContent = JSONUtil.toJsonStr(tokenContentOption);
        String token = jwtTokenComponent.generateToken(tokenContent);
        String refreshToken = jwtTokenComponent.generateRefreshToken(tokenContent);
        redisComponent.setWithExpire(TOKEN_KEY + ":" + userInfoVo.getId(), token,TOKEN_EXPIRE_TIME, TimeUnit.HOURS);
        studyJavaLoginVo.setToken(token);
        studyJavaLoginVo.setRefreshToken(refreshToken);

        return studyJavaLoginVo;
    }


    @Override
    public void logout() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String authorization = request.getHeader("Authorization");
            if (authorization != null) {
                String userInfo = jwtTokenComponent.getUserInfoFromAuthorization(authorization);
                Map<String, Object> map = JSONUtil.toBean(userInfo, Map.class);
                String userId = (String) map.get("userId");
                redisComponent.delete(TOKEN_KEY + ":" + userId);
            }
        }
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

    @Override
    public StudyJavaSysLoginVo refreshToken(String refreshToken) {
        if (jwtTokenComponent.isTokenExpired(refreshToken)) {
            throw new StudyJavaException("Refresh Token已过期，请重新登录");
        }
        String tokenContent = jwtTokenComponent.getUserInfoFromToken(refreshToken);

        // Generate new tokens
        String newToken = jwtTokenComponent.generateToken(tokenContent);
        String newRefreshToken = jwtTokenComponent.generateRefreshToken(tokenContent);

        StudyJavaSysLoginVo loginVo = new StudyJavaSysLoginVo();
        loginVo.setToken(newToken);
        loginVo.setRefreshToken(newRefreshToken);

        // Populate user info
        Map<String, Object> map = JSONUtil.toBean(tokenContent, Map.class);
        String loginName = (String) map.get("loginName");

        StudyJavaLoginDto loginDto = new StudyJavaLoginDto();
        loginDto.setUsername(loginName);
        StudyJavaSysUserVo userInfoVo = studyJavaSysUserService.getUserInfo(loginDto);

        if (userInfoVo != null) {
            loginVo.setId(userInfoVo.getId());
            loginVo.setLoginName(userInfoVo.getLoginName());
            loginVo.setAddress(userInfoVo.getAddress());
            loginVo.setCreateTime(userInfoVo.getCreateTime());
            loginVo.setIntroduceSign(userInfoVo.getIntroduceSign());
            loginVo.setNickName(userInfoVo.getNickName());

            // 更新 Redis 中的 Token
            redisComponent.setWithExpire(TOKEN_KEY + ":" + userInfoVo.getId(), newToken, TOKEN_EXPIRE_TIME, TimeUnit.HOURS);
        }

        return loginVo;
    }
}
