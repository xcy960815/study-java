package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.domain.StudyJavaAdminUser;

import java.util.List;


public interface StudyJavaAdminUserService {

   IPage<StudyJavaAdminUser> getAdminUserList(Page<StudyJavaAdminUser> page, StudyJavaAdminUser studyJavaAdminUser);
//   List<StudyJavaAdminUser> validateUser(StudyJavaAdminUser studyJavaAdminUser);
   int updateAdminUser(StudyJavaAdminUser studyJavaAdminUser);
   int insertAdminUser(StudyJavaAdminUser studyJavaAdminUser);
   int deleteAdminUser(StudyJavaAdminUser studyJavaAdminUser);
   boolean checkUser(StudyJavaAdminUser studyJavaAdminUser);
}
