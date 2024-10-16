package com.example.service.imp;
import com.example.domain.StudyJavaUser;
import com.example.mapper.StudyJavaUserMapper;
import com.example.service.StudyJavaUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class StudyJavaUserServiceImp implements StudyJavaUserService {

    @Resource
    private StudyJavaUserMapper studyJavaUserMapper;


    public List<StudyJavaUser> getAllUsers() {

        return studyJavaUserMapper.selectAllUsers();
    }

    public String getError(){
        return "我是 HelloWordServer 类的错误返回";
    }
}
