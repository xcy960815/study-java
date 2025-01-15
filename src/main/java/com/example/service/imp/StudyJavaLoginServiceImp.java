package com.example.service.imp;


import com.example.domain.vo.StudyJavaLoginVo;
import com.example.domain.dto.StudyJavaUserDto;
import com.example.service.StudyJavaLoginService;
import com.example.service.StudyJavaUserService;
import com.example.utils.JwtTokenUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class StudyJavaLoginServiceImp implements StudyJavaLoginService {
    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    private StudyJavaUserService studyJavaUserService;

    public String login(StudyJavaLoginVo studyJavaLoginParams) {

        StudyJavaUserDto loginUser = studyJavaUserService.getUserByNameAndPassword(studyJavaLoginParams);

        String loginName = loginUser.getLoginName();

        if (loginUser == null) {
            throw new RuntimeException("找不到用户或者密码不对");
        }

        return jwtTokenUtil.generateToken(loginName);
    }
}
