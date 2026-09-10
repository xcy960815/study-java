package com.studyjava.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.studyjava.component.JwtTokenComponent;
import com.studyjava.domain.dao.StudyJavaSysUserDao;
import com.studyjava.domain.dto.StudyJavaLoginDto;
import com.studyjava.domain.dto.StudyJavaSysUserDto;
import com.studyjava.domain.vo.StudyJavaSysUserVo;
import com.studyjava.exception.StudyJavaException;
import com.studyjava.mapper.StudyJavaSysMenuMapper;
import com.studyjava.mapper.StudyJavaSysUserMapper;
import com.studyjava.utils.PasswordUtils;

@ExtendWith(MockitoExtension.class)
class StudyJavaSysUserServiceImplTest {

  @Mock private StudyJavaSysUserMapper studyJavaSysUserMapper;
  @Mock private StudyJavaSysMenuMapper studyJavaSysMenuMapper;
  @Mock private JwtTokenComponent jwtTokenComponent;

  @InjectMocks private StudyJavaSysUserServiceImpl service;

  private StudyJavaSysUserDao storedUser;

  @BeforeEach
  void setUpRequestContext() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader("Authorization", "Bearer test-token");
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

    storedUser = new StudyJavaSysUserDao();
    storedUser.setId(1001L);
    storedUser.setLoginName("admin");
    storedUser.setPasswordMd5(PasswordUtils.encode("old-password"));
    storedUser.setRoleCodes(List.of("ORDINARY_USER"));
    lenient()
        .when(studyJavaSysUserMapper.getUserInfo(any(StudyJavaSysUserDao.class)))
        .thenReturn(storedUser);
    lenient()
        .when(jwtTokenComponent.getUserInfoFromAuthorization("Bearer test-token"))
        .thenReturn("{\"userId\":1001,\"loginName\":\"admin\"}");
  }

  @AfterEach
  void clearRequestContext() {
    RequestContextHolder.resetRequestAttributes();
  }

  private StudyJavaLoginDto loginDto(String loginName) {
    StudyJavaLoginDto dto = new StudyJavaLoginDto();
    dto.setUsername(loginName);
    return dto;
  }

  @Test
  void getUserInfoByLoginGrantsSuperPermissionsToSuperAdmin() {
    storedUser.setRoleCodes(List.of("SUPER_ADMIN"));

    StudyJavaSysUserVo vo = service.getUserInfo(loginDto("admin"));

    assertEquals(List.of("*:*:*"), vo.getPermissions());
    verify(studyJavaSysMenuMapper, never()).getPermsByUserId(any());
  }

  @Test
  void getUserInfoByLoginLoadsMenuPermsForNonSuperAdmin() {
    when(studyJavaSysMenuMapper.getPermsByUserId(1001L)).thenReturn(List.of("system:user:list"));

    StudyJavaSysUserVo vo = service.getUserInfo(loginDto("admin"));

    assertEquals(List.of("system:user:list"), vo.getPermissions());
  }

  @Test
  void getUserInfoByLoginReturnsNullWhenUserMissing() {
    when(studyJavaSysUserMapper.getUserInfo(any(StudyJavaSysUserDao.class))).thenReturn(null);

    StudyJavaSysUserVo vo = service.getUserInfo(loginDto("ghost"));

    assertNull(vo);
  }

  @Test
  void updateUserPasswordRejectsMismatchedConfirmation() {
    var dto = new StudyJavaSysUserDto();
    dto.setPasswordMd5("old-password");
    dto.setNewPasswordMd5("new-password");
    dto.setConfirmNewPasswordMd5("different-password");

    StudyJavaException exception =
        assertThrows(StudyJavaException.class, () -> service.updateUserPassword(dto));

    assertEquals("两次密码不一致", exception.getMessage());
    verify(studyJavaSysUserMapper, never()).updateUser(any());
  }

  @Test
  void updateUserPasswordRejectsWrongOldPassword() {
    var dto = new StudyJavaSysUserDto();
    dto.setPasswordMd5("wrong-old-password");
    dto.setNewPasswordMd5("new-password");
    dto.setConfirmNewPasswordMd5("new-password");

    StudyJavaException exception =
        assertThrows(StudyJavaException.class, () -> service.updateUserPassword(dto));

    assertEquals("原密码不正确", exception.getMessage());
    verify(studyJavaSysUserMapper, never()).updateUser(any());
  }

  @Test
  void updateUserPasswordHashesAndPersistsNewPassword() {
    when(studyJavaSysUserMapper.updateUser(any(StudyJavaSysUserDao.class))).thenReturn(1);
    var dto = new StudyJavaSysUserDto();
    dto.setPasswordMd5("old-password");
    dto.setNewPasswordMd5("new-password");
    dto.setConfirmNewPasswordMd5("new-password");

    Boolean result = service.updateUserPassword(dto);

    assertEquals(Boolean.TRUE, result);
    ArgumentCaptor<StudyJavaSysUserDao> captor =
        ArgumentCaptor.forClass(StudyJavaSysUserDao.class);
    verify(studyJavaSysUserMapper).updateUser(captor.capture());
    StudyJavaSysUserDao saved = captor.getValue();
    assertEquals(1001L, saved.getId());
    assertTrue(PasswordUtils.matches("new-password", saved.getPasswordMd5()));
    assertTrue(!PasswordUtils.needsUpgrade(saved.getPasswordMd5()));
  }

  @Test
  void insertUserHashesPlaintextPasswordBeforePersisting() {
    when(studyJavaSysUserMapper.insertUser(any(StudyJavaSysUserDao.class))).thenReturn(1);
    var dto = new StudyJavaSysUserDto();
    dto.setLoginName("new-user");
    dto.setPasswordMd5("plaintext-pass");

    Boolean result = service.insertUser(dto);

    assertEquals(Boolean.TRUE, result);
    ArgumentCaptor<StudyJavaSysUserDao> captor =
        ArgumentCaptor.forClass(StudyJavaSysUserDao.class);
    verify(studyJavaSysUserMapper).insertUser(captor.capture());
    assertTrue(PasswordUtils.matches("plaintext-pass", captor.getValue().getPasswordMd5()));
  }

  @Test
  void insertUserTranslatesDuplicateLoginNameToBusinessException() {
    when(studyJavaSysUserMapper.insertUser(any(StudyJavaSysUserDao.class)))
        .thenThrow(new DuplicateKeyException("Duplicate entry '13700002703'"));
    var dto = new StudyJavaSysUserDto();
    dto.setLoginName("13700002703");
    dto.setPasswordMd5("plaintext-pass");

    StudyJavaException exception =
        assertThrows(StudyJavaException.class, () -> service.insertUser(dto));

    assertEquals("该用户名已被注册", exception.getMessage());
  }

  @Test
  void insertUserLocksNewAccount() {
    when(studyJavaSysUserMapper.insertUser(any(StudyJavaSysUserDao.class))).thenReturn(1);
    var dto = new StudyJavaSysUserDto();
    dto.setLoginName("new-user");
    dto.setPasswordMd5("plaintext-pass");

    service.insertUser(dto);

    ArgumentCaptor<StudyJavaSysUserDao> captor =
        ArgumentCaptor.forClass(StudyJavaSysUserDao.class);
    verify(studyJavaSysUserMapper).insertUser(captor.capture());
    assertEquals(0, captor.getValue().getLockedFlag());
  }

  @Test
  void updateUserReplacesRoleBindings() {
    when(studyJavaSysUserMapper.updateUser(any(StudyJavaSysUserDao.class))).thenReturn(1);
    var dto = new StudyJavaSysUserDto();
    dto.setId(1001L);
    dto.setRoleIds(List.of(1L, 2L));

    Boolean result = service.updateUser(dto);

    assertEquals(Boolean.TRUE, result);
    verify(studyJavaSysUserMapper).deleteUserRolesByUserId(1001L);
    verify(studyJavaSysUserMapper).insertUserRolesBatch(1001L, List.of(1L, 2L));
  }

  @Test
  void updateUserSkipsRoleInsertWhenRoleIdsEmpty() {
    when(studyJavaSysUserMapper.updateUser(any(StudyJavaSysUserDao.class))).thenReturn(1);
    var dto = new StudyJavaSysUserDto();
    dto.setId(1001L);
    dto.setRoleIds(List.of());

    service.updateUser(dto);

    verify(studyJavaSysUserMapper).deleteUserRolesByUserId(1001L);
    verify(studyJavaSysUserMapper, never()).insertUserRolesBatch(any(), any());
  }

  @Test
  void deleteUserClearsRoleBindingsFirst() {
    when(studyJavaSysUserMapper.deleteUser(any(StudyJavaSysUserDao.class))).thenReturn(1);
    var dto = new StudyJavaSysUserDto();
    dto.setId(1001L);

    Boolean result = service.deleteUser(dto);

    assertEquals(Boolean.TRUE, result);
    verify(studyJavaSysUserMapper).deleteUserRolesByUserId(1001L);
    verify(studyJavaSysUserMapper).deleteUser(any(StudyJavaSysUserDao.class));
  }
}
