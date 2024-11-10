package com.example.service.imp;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.domain.StudyJavaUser;
import com.example.mapper.StudyJavaUserMapper;
import com.example.service.StudyJavaUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Date;
import java.util.List;


@Service
public class StudyJavaUserServiceImp implements StudyJavaUserService {

    @Resource
    private StudyJavaUserMapper studyJavaUserMapper;

    /**
     * 查询所有用户
     */
    @Override
    public IPage<StudyJavaUser>  getUserList(Page<StudyJavaUser> page ,StudyJavaUser userQueryData) {
        IPage<StudyJavaUser> pageResult = studyJavaUserMapper.getUserList(page,userQueryData);
        List<StudyJavaUser> userList = pageResult.getRecords();
        userList .forEach(user->{
            // 随机生成一个整数
            int age = (int)(Math.random()*100);
            user.setAge(age);
        });
        return pageResult;
    }
    @Override
    public int updateUser(StudyJavaUser studyJavaUser) {
        return studyJavaUserMapper.updateUser(studyJavaUser);
    }

    @Override
    public int insertUser(StudyJavaUser studyJavaUser) {
        studyJavaUser.setIsDeleted(0);
        studyJavaUser.setLockedFlag(0);
        Date createTime = new Date();
        studyJavaUser.setCreateTime(createTime);
        return studyJavaUserMapper.insertUser(studyJavaUser);
    }

    @Override
    public int deleteUser(StudyJavaUser studyJavaUser) {
        return studyJavaUserMapper.deleteUser(studyJavaUser);
    }

    @Override
    public boolean checkUser(StudyJavaUser studyJavaUser) {
        StudyJavaUser user = studyJavaUserMapper.checkUser(studyJavaUser);
        return user != null;
    }
}
