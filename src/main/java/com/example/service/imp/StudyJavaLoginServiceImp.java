package com.example.service.imp;


import com.example.domain.dto.StudyJavaLoginDto;
import com.example.domain.vo.StudyJavaLoginVo;
import com.example.domain.dto.StudyJavaUserDto;
import com.example.domain.vo.StudyJavaUserVo;
import com.example.service.StudyJavaLoginService;
import com.example.service.StudyJavaUserService;
import com.example.utils.JwtTokenUtil;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class StudyJavaLoginServiceImp implements StudyJavaLoginService {
    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    private StudyJavaUserService studyJavaUserService;

    public StudyJavaLoginDto login(StudyJavaLoginVo studyJavaLoginParams) {
        if(StringUtils.isBlank(studyJavaLoginParams.getUsername()) || StringUtils.isBlank(studyJavaLoginParams.getPassword())) {
            throw new RuntimeException("用户名或者密码不能为空");
        }
        StudyJavaUserVo studyJavaUserVo = new StudyJavaUserVo();
        studyJavaUserVo.setLoginName(studyJavaLoginParams.getUsername());
//        studyJavaUserVo.setPasswordMd5(studyJavaLoginParams.getPassword());

        StudyJavaUserDto userInfo = studyJavaUserService.getUserInfo(studyJavaUserVo);
        if (userInfo == null) {
            throw new RuntimeException("用户不存在");
        }
//        TODO
//        if(!userInfo.get().equals(studyJavaLoginParams.getPassword())) {
//            throw new RuntimeException("密码错误");
//        }
        StudyJavaLoginDto studyJavaLoginDto = new StudyJavaLoginDto();
        studyJavaLoginDto.setUserId(userInfo.getUserId());
        studyJavaLoginDto.setLoginName(userInfo.getLoginName());
        studyJavaLoginDto.setAddress(userInfo.getAddress());
        studyJavaLoginDto.setCreateTime(userInfo.getCreateTime());
        studyJavaLoginDto.setIntroduceSign(userInfo.getIntroduceSign());
        studyJavaLoginDto.setNickName(userInfo.getNickName());
//        将用户关键信息（能从数据库中查出来的字段保存进去）
        String tokenContent = userInfo.getUserId() + ":" + userInfo.getLoginName() + ":" + userInfo.getNickName();
        System.out.print("tokenContent---tokenContent"+tokenContent);
        studyJavaLoginDto.setToken(jwtTokenUtil.generateToken(tokenContent));
        return studyJavaLoginDto;
    }

        public void logout(StudyJavaLoginVo studyJavaLoginParams) {
//            studyJavaUserService.logout(studyJavaLoginParams);
        }
}
