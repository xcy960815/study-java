package com.example.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.domain.vo.StudyJavaAdminUserVo;
import com.example.domain.dto.StudyJavaAdminUserDto;
import com.example.service.StudyJavaAdminUserService;
import com.example.utils.ResponseGenerator;
import com.example.utils.ResponseResult;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping ("/adminUser")
public class StudyJavaAdminUserController {
    @Resource
    StudyJavaAdminUserService studyJavaAdminUserService;
    @RequestMapping("/getAdminUserList")
    @ResponseBody
    public ResponseResult getAdminUserList(
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @ModelAttribute("studyJavaAdminUser") StudyJavaAdminUserVo  studyJavaAdminUser
    ) {
        Page<StudyJavaAdminUserVo> adminUserPage = new Page<>(pageNum, pageSize);

        IPage<StudyJavaAdminUserDto> userPage = studyJavaAdminUserService.getAdminUserList(adminUserPage, studyJavaAdminUser);
        // 返回分页数据和总条数
        Map<String, Object> map = new HashMap<>();
        map.put("message","success");
        map.put("data", userPage.getRecords());
        map.put("total", userPage.getTotal());
        return ResponseGenerator.generatSuccessResult(map);
    }

    @PostMapping("/updateUser")
    @ResponseBody
    public ResponseResult updateUser(@RequestBody StudyJavaAdminUserVo studyJavaAdminUser) {
        Integer adminUserId = studyJavaAdminUser.getAdminUserId();
        if(adminUserId == null){
            return ResponseGenerator.generatErrorResult("用户ID不能为空");
        }

        studyJavaAdminUserService.updateAdminUser(studyJavaAdminUser);
        // 返回更新结果
        return ResponseGenerator.generatSuccessResult(true);
    };

    @PostMapping("/insertUser")
    @ResponseBody
    public ResponseResult insertUser(@RequestBody StudyJavaAdminUserVo studyJavaAdminUser) {
        String loginUserName = studyJavaAdminUser.getLoginUserName();
        if(!StringUtils.isNoneEmpty(loginUserName)){
            ResponseGenerator.generatErrorResult("用户名不能为空");
        }
        String nickName = studyJavaAdminUser.getNickName();
        if (!StringUtils.isNoneEmpty(nickName)){
            ResponseGenerator.generatErrorResult("昵称不能为空");
        }
        String loginPassWord = studyJavaAdminUser.getLoginPassword();
        if (!StringUtils.isNoneEmpty(loginPassWord)) {
            ResponseGenerator.generatErrorResult("密码不能为空");
        }
        int insertAdminUserResult = studyJavaAdminUserService.insertAdminUser(studyJavaAdminUser);
        // 返回插入结果
        return ResponseGenerator.generatSuccessResult(true);
    }

    @DeleteMapping("/deleteUser")
    @ResponseBody
    public  ResponseResult deleteUser(@RequestBody StudyJavaAdminUserVo studyJavaAdminUser) {
        Integer adminUserId = studyJavaAdminUser.getAdminUserId();
        if(adminUserId == null){
            return  ResponseGenerator.generatErrorResult("用户ID不能为空");
        }
        studyJavaAdminUserService.deleteAdminUser(studyJavaAdminUser);
        // 返回插入结果
        return ResponseGenerator.generatSuccessResult(true);
    }
}
