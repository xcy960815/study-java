package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.domain.dao.StudyJavaAdminUserDao;
//import com.example.domain.dto.StudyJavaAdminUserDto;
import com.example.domain.dto.StudyJavaAdminUserDto;
import com.example.domain.vo.StudyJavaAdminUserVo;


public interface StudyJavaAdminUserService {

   IPage<StudyJavaAdminUserVo> getAdminUserList(Page<StudyJavaAdminUserDto> page, StudyJavaAdminUserDto studyJavaAdminUser);
   int updateAdminUser(StudyJavaAdminUserDto studyJavaAdminUser);
   int insertAdminUser(StudyJavaAdminUserDto studyJavaAdminUser);
   int deleteAdminUser(StudyJavaAdminUserDto studyJavaAdminUser);
}
