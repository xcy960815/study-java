package com.example.service.imp;


import com.example.domain.dao.StudyJavaUserDao;
import com.example.domain.dto.StudyJavaLoginDto;
import com.example.domain.vo.StudyJavaLoginVo;
import org.springframework.util.DigestUtils;
import com.example.domain.vo.StudyJavaUserVo;
import com.example.mapper.StudyJavaUserMapper;
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

    @Resource
    private StudyJavaUserMapper studyJavaUserMapper;

    @Override
    public StudyJavaLoginDto login(StudyJavaLoginVo studyJavaLoginParams)  {
        // 账号密码校验
        if(StringUtils.isBlank(studyJavaLoginParams.getUsername()) || StringUtils.isBlank(studyJavaLoginParams.getPassword())) {
            throw new RuntimeException("用户名或者密码不能为空");
        }
        StudyJavaUserVo studyJavaUserVo = new StudyJavaUserVo();

        studyJavaUserVo.setLoginName(studyJavaLoginParams.getUsername());

        StudyJavaUserDao userInfo = studyJavaUserMapper.getUserInfo(studyJavaUserVo);

        if (userInfo == null) {
            throw new RuntimeException("用户不存在");
        }

        if(!StringUtils.isBlank(userInfo.getPasswordMd5()) && !StringUtils.isBlank(studyJavaLoginParams.getPassword())) {
//            String dataBasePassword = DigestUtils.md5DigestAsHex(userInfo.getPasswordMd5().getBytes());
//            String loginPassword = DigestUtils.md5DigestAsHex(studyJavaLoginParams.getPassword().getBytes());
            String dataBasePassword = userInfo.getPasswordMd5();
            String loginPassword = studyJavaLoginParams.getPassword();
            if(!dataBasePassword.equals(loginPassword)){
                throw new RuntimeException("密码错误");
            }
        }
        StudyJavaLoginDto studyJavaLoginDto = new StudyJavaLoginDto();
        studyJavaLoginDto.setUserId(userInfo.getUserId());
        studyJavaLoginDto.setLoginName(userInfo.getLoginName());
        studyJavaLoginDto.setAddress(userInfo.getAddress());
        studyJavaLoginDto.setCreateTime(userInfo.getCreateTime());
        studyJavaLoginDto.setIntroduceSign(userInfo.getIntroduceSign());
        studyJavaLoginDto.setNickName(userInfo.getNickName());
        // 将用户关键信息（能从数据库中查出来的字段保存进去）
        String tokenContent = userInfo.getUserId() + ":" + userInfo.getLoginName() + ":" + userInfo.getNickName();
        studyJavaLoginDto.setToken(jwtTokenUtil.generateToken(tokenContent));
        return studyJavaLoginDto;
    }

        public void logout(StudyJavaLoginVo studyJavaLoginParams) {
            // studyJavaUserService.logout(studyJavaLoginParams);
        }
}
