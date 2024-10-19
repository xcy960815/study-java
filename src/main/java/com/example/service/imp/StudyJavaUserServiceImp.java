package com.example.service.imp;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.domain.StudyJavaUser;
import com.example.dto.StudyJavaUserDTO;
import com.example.mapper.StudyJavaUserMapper;
import com.example.service.StudyJavaUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;


@Service
public class StudyJavaUserServiceImp implements StudyJavaUserService {

    @Resource
    private StudyJavaUserMapper studyJavaUserMapper;

    /**
     * 查询所有用户
     * @return List<StudyJavaUser>
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
}
