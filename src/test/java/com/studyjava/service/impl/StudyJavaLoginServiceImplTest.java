package com.studyjava.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.code.kaptcha.Producer;
import com.studyjava.component.JwtTokenComponent;
import com.studyjava.component.RedisComponent;
import com.studyjava.domain.dao.StudyJavaSysUserDao;
import com.studyjava.domain.dto.StudyJavaLoginDto;
import com.studyjava.domain.vo.StudyJavaSysUserVo;
import com.studyjava.exception.StudyJavaException;
import com.studyjava.mapper.StudyJavaSysUserMapper;
import com.studyjava.service.StudyJavaSysUserService;
import com.studyjava.utils.AuthRedisKeys;

@ExtendWith(MockitoExtension.class)
class StudyJavaLoginServiceImplTest {

  private static final String CAPTCHA_ID = "captcha-id-1";

  @Mock private JwtTokenComponent jwtTokenComponent;
  @Mock private RedisComponent redisComponent;
  @Mock private Producer kaptchaProducer;
  @Mock private StudyJavaSysUserService studyJavaSysUserService;
  @Mock private StudyJavaSysUserMapper studyJavaSysUserMapper;

  @InjectMocks private StudyJavaLoginServiceImpl loginService;

  private StudyJavaLoginDto loginDto(String username, String password, String captcha) {
    StudyJavaLoginDto dto = new StudyJavaLoginDto();
    dto.setUsername(username);
    dto.setPassword(password);
    dto.setCaptchaId(CAPTCHA_ID);
    dto.setCaptcha(captcha);
    return dto;
  }

  @Test
  void loginRejectsExpiredCaptchaWithoutTouchingUserService() {
    when(redisComponent.getAndDelete(AuthRedisKeys.captchaKey(CAPTCHA_ID))).thenReturn(null);

    StudyJavaException exception =
        assertThrows(
            StudyJavaException.class, () -> loginService.login(loginDto("admin", "pw", "abcd")));

    assertEquals("验证码不存在", exception.getMessage());
    verify(studyJavaSysUserService, never()).getUserInfo(any(StudyJavaLoginDto.class));
  }

  @Test
  void loginUsesUnifiedMessageForUnknownUser() {
    when(redisComponent.getAndDelete(AuthRedisKeys.captchaKey(CAPTCHA_ID))).thenReturn("abcd");
    when(studyJavaSysUserService.getUserInfo(any(StudyJavaLoginDto.class))).thenReturn(null);

    StudyJavaException exception =
        assertThrows(
            StudyJavaException.class, () -> loginService.login(loginDto("ghost", "pw", "abcd")));

    // 统一文案，防止区分"用户不存在"/"密码错误"被用于枚举用户名
    assertEquals("用户名或密码错误", exception.getMessage());
  }

  @Test
  void loginUsesUnifiedMessageForWrongPassword() {
    when(redisComponent.getAndDelete(AuthRedisKeys.captchaKey(CAPTCHA_ID))).thenReturn("abcd");
    StudyJavaSysUserVo userVo = new StudyJavaSysUserVo();
    userVo.setId(7L);
    userVo.setLoginName("admin");
    userVo.setPasswordMd5("not-bcrypt-of-pw");
    when(studyJavaSysUserService.getUserInfo(any(StudyJavaLoginDto.class))).thenReturn(userVo);

    StudyJavaException exception =
        assertThrows(
            StudyJavaException.class, () -> loginService.login(loginDto("admin", "pw", "abcd")));

    assertEquals("用户名或密码错误", exception.getMessage());
  }

  @Test
  void loginUpgradesLegacyPasswordAfterSuccessfulMatch() {
    when(redisComponent.getAndDelete(AuthRedisKeys.captchaKey(CAPTCHA_ID))).thenReturn("abcd");
    StudyJavaSysUserVo userVo = new StudyJavaSysUserVo();
    userVo.setId(7L);
    userVo.setLoginName("admin");
    userVo.setPasswordMd5("e10adc3949ba59abbe56e057f20f883e");
    when(studyJavaSysUserService.getUserInfo(any(StudyJavaLoginDto.class))).thenReturn(userVo);

    loginService.login(loginDto("admin", "123456", "abcd"));

    verify(studyJavaSysUserMapper)
        .updateUser(
            argThat(
                (StudyJavaSysUserDao dao) ->
                    Long.valueOf(7L).equals(dao.getId())
                        && dao.getPasswordMd5() != null
                        && dao.getPasswordMd5().startsWith("$2")));
  }

  @Test
  void refreshTokenFailsFastWhenUserNoLongerExists() {
    when(jwtTokenComponent.isTokenExpired("stale-refresh-token")).thenReturn(false);
    when(jwtTokenComponent.getUserInfoFromToken("stale-refresh-token"))
        .thenReturn("{\"userId\":\"7\",\"loginName\":\"ghost\"}");
    when(redisComponent.get(AuthRedisKeys.refreshTokenKey("7"), String.class))
        .thenReturn("stale-refresh-token");
    when(studyJavaSysUserService.getUserInfo(any(StudyJavaLoginDto.class))).thenReturn(null);

    StudyJavaException exception =
        assertThrows(
            StudyJavaException.class, () -> loginService.refreshToken("stale-refresh-token"));

    assertEquals("用户不存在，请重新登录", exception.getMessage());
    // 不应签发任何新 token
    verify(jwtTokenComponent, never()).generateToken(any());
    verify(jwtTokenComponent, never()).generateRefreshToken(any());
  }

  @Test
  void registerRejectsExpiredCaptchaWithoutCreatingUser() {
    com.studyjava.domain.dto.StudyJavaRegisterDto registerDto =
        new com.studyjava.domain.dto.StudyJavaRegisterDto();
    registerDto.setUsername("newbie");
    registerDto.setPassword("pw");
    registerDto.setConfirmPassword("pw");
    registerDto.setCaptchaId(CAPTCHA_ID);
    registerDto.setCaptcha("abcd");

    when(redisComponent.getAndDelete(AuthRedisKeys.captchaKey(CAPTCHA_ID))).thenReturn(null);

    StudyJavaException exception =
        assertThrows(StudyJavaException.class, () -> loginService.register(registerDto));

    assertEquals("验证码不存在或已过期", exception.getMessage());
    verify(studyJavaSysUserService, never()).insertUser(any());
  }
}
