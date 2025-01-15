package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.domain.dao.StudyJavaAdminUserDao;
//import com.example.domain.dto.StudyJavaAdminUserDto;
import com.example.domain.dto.StudyJavaAdminUserDto;
import com.example.domain.vo.StudyJavaAdminUserVo;


public interface StudyJavaAdminUserService {

   IPage<StudyJavaAdminUserDto> getAdminUserList(Page<StudyJavaAdminUserVo> page, StudyJavaAdminUserVo studyJavaAdminUser);
   int updateAdminUser(StudyJavaAdminUserVo studyJavaAdminUser);
   int insertAdminUser(StudyJavaAdminUserVo studyJavaAdminUser);
   int deleteAdminUser(StudyJavaAdminUserVo studyJavaAdminUser);
}
