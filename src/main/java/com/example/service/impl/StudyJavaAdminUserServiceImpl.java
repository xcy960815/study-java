package com.example.service.impl;

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
    public IPage<StudyJavaAdminUserVo> getAdminUserList(Page<StudyJavaAdminUserDto> page, StudyJavaAdminUserDto studyJavaAdminUser) {

        // 调用 Mapper 获取分页结果
        IPage<StudyJavaAdminUserDao> adminUserPageResult = studyJavaAdminUserMapper.getAdminUserList(page, studyJavaAdminUser);

        // 将 StudyJavaAdminUserDao 转换成 StudyJavaAdminUserVo
        List<StudyJavaAdminUserVo> userList = adminUserPageResult.getRecords().stream().map(user -> {
            StudyJavaAdminUserVo studyJavaAdminUserVo = new StudyJavaAdminUserVo();
            studyJavaAdminUserVo.setAdminUserId(user.getAdminUserId());
            studyJavaAdminUserVo.setLoginUserName(user.getLoginUserName());
            studyJavaAdminUserVo.setNickName(user.getNickName());
            studyJavaAdminUserVo.setLocked(user.getLocked());
            return studyJavaAdminUserVo;
        }).toList();

        // 创建新的 IPage 对象
        IPage<StudyJavaAdminUserVo> resultPage = new Page<>(adminUserPageResult.getCurrent(), adminUserPageResult.getSize(), adminUserPageResult.getTotal());
        resultPage.setRecords(userList);

        return resultPage;
    }
    @Override
    public int updateAdminUser(StudyJavaAdminUserDto studyJavaAdminUser){
        return studyJavaAdminUserMapper.updateAdminUser(studyJavaAdminUser);
    }

    @Override
    public int insertAdminUser(StudyJavaAdminUserDto studyJavaAdminUser) {
        return studyJavaAdminUserMapper.insertAdminUser(studyJavaAdminUser);
    }

    @Override
    public int deleteAdminUser(StudyJavaAdminUserDto studyJavaAdminUser) {
        return studyJavaAdminUserMapper.deleteAdminUser(studyJavaAdminUser);
    }
}
