package com.studyjava.service.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.code.kaptcha.Producer;
import com.studyjava.component.JwtTokenComponent;
import com.studyjava.component.RedisComponent;
import com.studyjava.domain.dao.StudyJavaSysUserDao;
import com.studyjava.domain.dto.StudyJavaLoginDto;
import com.studyjava.domain.dto.StudyJavaSysUserDto;
import com.studyjava.domain.vo.StudyJavaCaptchaVo;
import com.studyjava.domain.vo.StudyJavaSysLoginVo;
import com.studyjava.domain.vo.StudyJavaSysUserVo;
import com.studyjava.exception.StudyJavaException;
import com.studyjava.mapper.StudyJavaSysUserMapper;
import com.studyjava.service.StudyJavaLoginService;
import com.studyjava.service.StudyJavaSysUserService;
import com.studyjava.utils.AuthRedisKeys;
import com.studyjava.utils.PasswordUtils;

import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StudyJavaLoginServiceImpl implements StudyJavaLoginService {

  /** 验证码 key 名 */
  private static final int CAPTCHA_EXPIRE_TIME_MINUTES = 5;

  /** token 过期时间 */
  private static final int TOKEN_EXPIRE_TIME_HOURS = 24;

  /** refresh token 过期时间 */
  private static final int REFRESH_TOKEN_EXPIRE_TIME_DAYS = 7;

  @Resource private JwtTokenComponent jwtTokenComponent;

  @Resource private RedisComponent redisComponent;

  @Resource private Producer kaptchaProducer;

  @Resource private StudyJavaSysUserService studyJavaSysUserService;

  @Resource private StudyJavaSysUserMapper studyJavaSysUserMapper;

  @Override
  public StudyJavaSysLoginVo login(StudyJavaLoginDto studyJavaLoginDto) {
    String captchaKey = AuthRedisKeys.captchaKey(studyJavaLoginDto.getCaptchaId());
    if (!redisComponent.hasKey(captchaKey)) {
      throw new StudyJavaException("验证码不存在");
    }

    String captcha = redisComponent.get(captchaKey, String.class);
    redisComponent.delete(captchaKey);

    if (!captcha.equalsIgnoreCase(studyJavaLoginDto.getCaptcha())) {

      throw new StudyJavaException("验证码错误");
    }
    StudyJavaSysUserDto studyJavaSysUserDto = new StudyJavaSysUserDto();

    studyJavaSysUserDto.setLoginName(studyJavaLoginDto.getUsername());

    StudyJavaSysUserVo userInfoVo = studyJavaSysUserService.getUserInfo(studyJavaLoginDto);

    if (userInfoVo == null) {
      throw new StudyJavaException("用户不存在");
    }

    String dataBasePassword = userInfoVo.getPasswordMd5();
    String loginPassword = studyJavaLoginDto.getPassword();
    if (StringUtils.isBlank(dataBasePassword)
        || StringUtils.isBlank(loginPassword)
        || !PasswordUtils.matches(loginPassword, dataBasePassword)) {
      throw new StudyJavaException("密码错误");
    }

    if (PasswordUtils.needsUpgrade(dataBasePassword)) {
      StudyJavaSysUserDao updatePasswordDao = new StudyJavaSysUserDao();
      updatePasswordDao.setId(userInfoVo.getId());
      updatePasswordDao.setPasswordMd5(PasswordUtils.encode(loginPassword));
      studyJavaSysUserMapper.updateUser(updatePasswordDao);
    }
    StudyJavaSysLoginVo studyJavaLoginVo = new StudyJavaSysLoginVo();
    studyJavaLoginVo.setId(userInfoVo.getId());
    studyJavaLoginVo.setLoginName(userInfoVo.getLoginName());
    studyJavaLoginVo.setAddress(userInfoVo.getAddress());
    studyJavaLoginVo.setCreateTime(userInfoVo.getCreateTime());
    studyJavaLoginVo.setIntroduceSign(userInfoVo.getIntroduceSign());
    studyJavaLoginVo.setNickName(userInfoVo.getNickName());
    Map<String, String> tokenContentOption = new HashMap<>();
    tokenContentOption.put("loginName", userInfoVo.getLoginName());
    tokenContentOption.put("userId", userInfoVo.getId().toString());
    // 将用户关键信息（能从数据库中查出来的字段保存进去）
    String tokenContent = JSONUtil.toJsonStr(tokenContentOption);
    String token = jwtTokenComponent.generateToken(tokenContent);
    String refreshToken = jwtTokenComponent.generateRefreshToken(tokenContent);
    redisComponent.setWithExpire(
        AuthRedisKeys.accessTokenKey(userInfoVo.getId().toString()),
        token,
        TOKEN_EXPIRE_TIME_HOURS,
        TimeUnit.HOURS);
    redisComponent.setWithExpire(
        AuthRedisKeys.refreshTokenKey(userInfoVo.getId().toString()),
        refreshToken,
        REFRESH_TOKEN_EXPIRE_TIME_DAYS,
        TimeUnit.DAYS);
    studyJavaLoginVo.setToken(token);
    studyJavaLoginVo.setRefreshToken(refreshToken);

    return studyJavaLoginVo;
  }

  @Override
  public void register(com.studyjava.domain.dto.StudyJavaRegisterDto studyJavaRegisterDto) {
    String captchaKey = AuthRedisKeys.captchaKey(studyJavaRegisterDto.getCaptchaId());
    if (!redisComponent.hasKey(captchaKey)) {
      throw new StudyJavaException("验证码不存在或已过期");
    }

    String captcha = redisComponent.get(captchaKey, String.class);
    redisComponent.delete(captchaKey);
    if (!captcha.equalsIgnoreCase(studyJavaRegisterDto.getCaptcha())) {
      throw new StudyJavaException("验证码错误");
    }

    if (!studyJavaRegisterDto.getPassword().equals(studyJavaRegisterDto.getConfirmPassword())) {
      throw new StudyJavaException("两次输入的密码不一致");
    }

    StudyJavaLoginDto loginDtoCheck = new StudyJavaLoginDto();
    loginDtoCheck.setUsername(studyJavaRegisterDto.getUsername());
    if (studyJavaSysUserService.getUserInfo(loginDtoCheck) != null) {
      throw new StudyJavaException("该用户名已被注册");
    }

    StudyJavaSysUserDto newUser = new StudyJavaSysUserDto();
    newUser.setLoginName(studyJavaRegisterDto.getUsername());
    newUser.setPasswordMd5(PasswordUtils.encode(studyJavaRegisterDto.getPassword()));
    newUser.setNickName(studyJavaRegisterDto.getUsername());
    studyJavaSysUserService.insertUser(newUser);
  }

  @Override
  public void logout() {
    ServletRequestAttributes attributes =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    if (attributes != null) {
      HttpServletRequest request = attributes.getRequest();
      String authorization = request.getHeader("Authorization");
      if (authorization != null) {
        String userInfo = jwtTokenComponent.getUserInfoFromAuthorization(authorization);
        Map<String, Object> map = JSONUtil.toBean(userInfo, Map.class);
        String userId = (String) map.get("userId");
        redisComponent.delete(AuthRedisKeys.accessTokenKey(userId));
        redisComponent.delete(AuthRedisKeys.refreshTokenKey(userId));
      }
    }
  }

  /**
   * 生成验证码
   *
   * @return String
   * @throws IOException
   */
  public StudyJavaCaptchaVo getCaptcha() throws IOException {
    long startTime = System.currentTimeMillis(); // 获取开始时间(毫秒)
    // 生成验证码文本
    String captchaText = kaptchaProducer.createText();
    String captchaId = UUID.randomUUID().toString();
    redisComponent.setWithExpire(
        AuthRedisKeys.captchaKey(captchaId),
        captchaText,
        CAPTCHA_EXPIRE_TIME_MINUTES,
        TimeUnit.MINUTES);
    BufferedImage captchaImage = kaptchaProducer.createImage(captchaText);
    // 将验证码图像转换为 Base64
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    ImageIO.write(captchaImage, "jpg", byteArrayOutputStream);
    long endTime = System.currentTimeMillis(); // 获取结束时间(毫秒)
    // 毫秒转秒 并保留2位小数
    log.info("生成验证码耗时 {}", String.format("%.2f", (endTime - startTime) / 1000.0) + "秒");
    return new StudyJavaCaptchaVo(
        captchaId,
        "data:image/jpeg;base64,"
            + Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray()));
  }

  @Override
  public StudyJavaSysLoginVo refreshToken(String refreshToken) {
    if (jwtTokenComponent.isTokenExpired(refreshToken)) {
      throw new StudyJavaException("Refresh Token已过期，请重新登录");
    }
    String tokenContent = jwtTokenComponent.getUserInfoFromToken(refreshToken);
    Map<String, Object> map = JSONUtil.toBean(tokenContent, Map.class);
    String userId = (String) map.get("userId");
    String storedRefreshToken = redisComponent.get(AuthRedisKeys.refreshTokenKey(userId), String.class);
    if (!refreshToken.equals(storedRefreshToken)) {
      throw new StudyJavaException("Refresh Token无效，请重新登录");
    }

    // Generate new tokens
    String newToken = jwtTokenComponent.generateToken(tokenContent);
    String newRefreshToken = jwtTokenComponent.generateRefreshToken(tokenContent);

    StudyJavaSysLoginVo loginVo = new StudyJavaSysLoginVo();
    loginVo.setToken(newToken);
    loginVo.setRefreshToken(newRefreshToken);

    // Populate user info
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
      redisComponent.setWithExpire(
          AuthRedisKeys.accessTokenKey(userInfoVo.getId().toString()),
          newToken,
          TOKEN_EXPIRE_TIME_HOURS,
          TimeUnit.HOURS);
      redisComponent.setWithExpire(
          AuthRedisKeys.refreshTokenKey(userInfoVo.getId().toString()),
          newRefreshToken,
          REFRESH_TOKEN_EXPIRE_TIME_DAYS,
          TimeUnit.DAYS);
    }

    return loginVo;
  }
}
