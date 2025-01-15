package com.example.service.imp;


import com.example.domain.dto.StudyJavaLoginDto;
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

    public StudyJavaLoginDto login(StudyJavaLoginVo studyJavaLoginParams) {

        StudyJavaUserDto loginUser = studyJavaUserService.getUserByNameAndPassword(studyJavaLoginParams);
        if (loginUser == null) {
            throw new RuntimeException("找不到用户或者密码不对");
        }
        StudyJavaLoginDto studyJavaLoginDto = new StudyJavaLoginDto();
//        studyJavaLoginDto.setNickName(loginUser.getNickName());
        studyJavaLoginDto.setId(loginUser.getId());
        studyJavaLoginDto.setLoginName(loginUser.getLoginName());
        studyJavaLoginDto.setAddress(loginUser.getAddress());
        studyJavaLoginDto.setCreateTime(loginUser.getCreateTime());
        studyJavaLoginDto.setIntroduceSign(loginUser.getIntroduceSign());
        studyJavaLoginDto.setNickName(loginUser.getNickName());
        studyJavaLoginDto.setToken(jwtTokenUtil.generateToken(loginUser.getLoginName()));

        return studyJavaLoginDto;
    }
}
