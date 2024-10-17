package com.example.service.imp;
import com.example.domain.StudyJavaUser;
import com.example.dto.StudyJavaUserDTO;
import com.example.mapper.StudyJavaUserMapper;
import com.example.service.StudyJavaUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class StudyJavaUserServiceImp implements StudyJavaUserService {

    @Resource
    private StudyJavaUserMapper studyJavaUserMapper;


    /**
    * 查询所有用户
    * @return List<StudyJavaUser>
    */
    public List<StudyJavaUser> getAllUsers() {
        List<StudyJavaUser> userList =  studyJavaUserMapper.selectAllUsers();
        userList.forEach(user->{
            user.setAge("我是动态生成的年龄");
            System.out.println("对象的isDeleted的属性"+user.getIsDeleted());
            System.out.println("对象的getLockedFlag的属性"+user.getLockedFlag());
        });
        return userList;
    }

    /**
     * 查询所有用户 通过dto的方式构造数据结构，去掉一些前端用不到的字段，添加前段需要的字段
     * @return List<StudyJavaUserDTO>
     */
    public List<StudyJavaUserDTO> getAllUsersDTO(){
        List<StudyJavaUserDTO> studyJavaUserDTO = new ArrayList<>();
        List<StudyJavaUser> userList =  studyJavaUserMapper.selectAllUsers();
        // for 循环
        for(StudyJavaUser user:userList){
            StudyJavaUserDTO userDTO = new StudyJavaUserDTO();
            userDTO.setId(user.getUserId());
            userDTO.setAge(18);
            userDTO.setNickName(user.getNickName());
            userDTO.setLoginName(user.getLoginName());
            userDTO.setAddress(user.getAddress());
            userDTO.setCreateTime(user.getCreateTime());
            userDTO.setIntroduceSign(user.getIntroduceSign());
            studyJavaUserDTO.add(userDTO);
        }
        return  studyJavaUserDTO;
    }

    public String getError(){
        return "我是 HelloWordServer 类的错误返回";
    }
}
