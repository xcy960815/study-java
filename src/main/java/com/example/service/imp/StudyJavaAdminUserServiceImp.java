package com.example.service.imp;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.domain.StudyJavaAdminUser;
import com.example.mapper.StudyJavaAdminUserMapper;
import com.example.service.StudyJavaAdminUserService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudyJavaAdminUserServiceImp implements StudyJavaAdminUserService{
    @Resource
    StudyJavaAdminUserMapper studyJavaAdminUserMapper;

    @Override
    public IPage<StudyJavaAdminUser> getAdminUserList(Page<StudyJavaAdminUser> page, StudyJavaAdminUser studyJavaAdminUser) {
        return studyJavaAdminUserMapper.getAdminUserList(page, studyJavaAdminUser);
    }
    @Override
    public int updateAdminUser(StudyJavaAdminUser studyJavaAdminUser){
        return studyJavaAdminUserMapper.updateAdminUser(studyJavaAdminUser);
    };

    @Override
    public int insertAdminUser(StudyJavaAdminUser studyJavaAdminUser) {
        return studyJavaAdminUserMapper.insertAdminUser(studyJavaAdminUser);
    }

    @Override
    public int deleteAdminUser(StudyJavaAdminUser studyJavaAdminUser) {
        return studyJavaAdminUserMapper.deleteAdminUser(studyJavaAdminUser);
    }

    @Override
    public boolean checkUser(StudyJavaAdminUser studyJavaAdminUser) {
        StudyJavaAdminUser adminUser = studyJavaAdminUserMapper.checkUser(studyJavaAdminUser);
        return adminUser != null;
    }
}
