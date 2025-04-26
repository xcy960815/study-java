package com.example.service.imp;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.domain.dao.StudyJavaAdminUserDao;
import com.example.domain.dto.StudyJavaAdminUserDto;
import com.example.domain.vo.StudyJavaAdminUserVo;
import com.example.mapper.StudyJavaAdminUserMapper;
import com.example.service.StudyJavaAdminUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudyJavaAdminUserServiceImpl implements StudyJavaAdminUserService{
    @Resource
    StudyJavaAdminUserMapper studyJavaAdminUserMapper;

    @Override
    public IPage<StudyJavaAdminUserDto> getAdminUserList(Page<StudyJavaAdminUserVo> page, StudyJavaAdminUserVo studyJavaAdminUser) {

        // 调用 Mapper 获取分页结果
        IPage<StudyJavaAdminUserDao> adminUserPageResult = studyJavaAdminUserMapper.getAdminUserList(page, studyJavaAdminUser);

        // 将 StudyJavaAdminUserDao 转换成 StudyJavaAdminUserDto
        List<StudyJavaAdminUserDto> userList = adminUserPageResult.getRecords().stream().map(user -> {
            StudyJavaAdminUserDto studyJavaAdminUserDto = new StudyJavaAdminUserDto();
            studyJavaAdminUserDto.setAdminUserId(user.getAdminUserId());
            studyJavaAdminUserDto.setLoginUserName(user.getLoginUserName());
            studyJavaAdminUserDto.setNickName(user.getNickName());
            return studyJavaAdminUserDto;
        }).toList();

        // 创建新的 IPage 对象
        IPage<StudyJavaAdminUserDto> resultPage = new Page<>(adminUserPageResult.getCurrent(), adminUserPageResult.getSize(), adminUserPageResult.getTotal());
        resultPage.setRecords(userList);

        return resultPage;
    }
    @Override
    public int updateAdminUser(StudyJavaAdminUserVo studyJavaAdminUser){
        return studyJavaAdminUserMapper.updateAdminUser(studyJavaAdminUser);
    }

    @Override
    public int insertAdminUser(StudyJavaAdminUserVo studyJavaAdminUser) {
        return studyJavaAdminUserMapper.insertAdminUser(studyJavaAdminUser);
    }

    @Override
    public int deleteAdminUser(StudyJavaAdminUserVo studyJavaAdminUser) {
        return studyJavaAdminUserMapper.deleteAdminUser(studyJavaAdminUser);
    }
}
