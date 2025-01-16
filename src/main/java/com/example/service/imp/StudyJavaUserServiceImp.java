package com.example.service.imp;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.domain.dao.StudyJavaUserDao;
import com.example.domain.vo.StudyJavaLoginVo;
import com.example.domain.dto.StudyJavaUserDto;
import com.example.domain.vo.StudyJavaUserVo;
import com.example.mapper.StudyJavaUserMapper;
import com.example.service.StudyJavaUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Date;
import java.util.List;
import java.util.Random;


@Service
public class StudyJavaUserServiceImp implements StudyJavaUserService {

    @Resource
    private StudyJavaUserMapper studyJavaUserMapper;

    /**
     * 查询所有用户
     */
    @Override
    public IPage<StudyJavaUserDto> getUserList(Page<StudyJavaUserVo> page ,StudyJavaUserVo userQueryData) {

        IPage<StudyJavaUserDao> userPageResult = studyJavaUserMapper.getUserList(page,userQueryData);
        List<StudyJavaUserDto> userList = userPageResult.getRecords().stream().map(user -> {
            StudyJavaUserDto studyJavaUserDto = new StudyJavaUserDto();
            studyJavaUserDto.setId(user.getUserId());
            studyJavaUserDto.setAddress(user.getAddress());
            studyJavaUserDto.setNickName(user.getNickName());
            studyJavaUserDto.setLoginName(user.getLoginName());
            studyJavaUserDto.setIntroduceSign(user.getIntroduceSign());
            studyJavaUserDto.setCreateTime(user.getCreateTime());
            // 年龄 随机生成
            Random random = new Random();
            Integer age = random.nextInt(100);
            studyJavaUserDto.setAge(age);
            return studyJavaUserDto;
        }).toList();
        // 创建新的 IPage 对象
        IPage<StudyJavaUserDto> resultPage = new Page<>(userPageResult.getCurrent(), userPageResult.getSize(), userPageResult.getTotal());
        resultPage.setRecords(userList);

        return resultPage;
    }
    @Override
    public int updateUser(StudyJavaUserVo studyJavaUser) {
        return studyJavaUserMapper.updateUser(studyJavaUser);
    }

    @Override
    public int insertUser(StudyJavaUserVo studyJavaUser) {
        studyJavaUser.setIsDeleted(0);
        studyJavaUser.setLockedFlag(0);
        Date createTime = new Date();
        studyJavaUser.setCreateTime(createTime);
        return studyJavaUserMapper.insertUser(studyJavaUser);
    }

    @Override
    public int deleteUser(StudyJavaUserVo studyJavaUser) {
        return studyJavaUserMapper.deleteUser(studyJavaUser);
    }


    @Override
    public StudyJavaUserDto getUserInfo(StudyJavaUserVo studyJavaUserVo){
        StudyJavaUserDao userInfoDao = studyJavaUserMapper.getUserInfo(studyJavaUserVo);
        StudyJavaUserDto userInfoDto = new StudyJavaUserDto();
        userInfoDto.setId(userInfoDao.getUserId());
        userInfoDto.setAddress(userInfoDao.getAddress());
        userInfoDto.setNickName(userInfoDao.getNickName());
        userInfoDto.setLoginName(userInfoDao.getLoginName());
        userInfoDto.setIntroduceSign(userInfoDao.getIntroduceSign());
        userInfoDto.setCreateTime(userInfoDao.getCreateTime());
        // 年龄 随机生成
        Random random = new Random();
        Integer age = random.nextInt(100);
        userInfoDto.setAge(age);

        return userInfoDto;
    }
}
