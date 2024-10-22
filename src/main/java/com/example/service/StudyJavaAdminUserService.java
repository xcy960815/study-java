package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.domain.StudyJavaAdminUser;




public interface StudyJavaAdminUserService {

   IPage<StudyJavaAdminUser> getAdminUserList(Page<StudyJavaAdminUser> page, StudyJavaAdminUser studyJavaAdminUser);

   int updateAdminUser(StudyJavaAdminUser studyJavaAdminUser);
   int insertAdminUser(StudyJavaAdminUser studyJavaAdminUser);
   int deleteAdminUser(StudyJavaAdminUser studyJavaAdminUser);
}
