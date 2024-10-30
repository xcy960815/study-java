package com.example.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.domain.StudyJavaUser;
import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.utils.ResponseResult;
import com.example.utils.ResponseGenerator;
import com.example.service.StudyJavaUserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user") // 前缀
public class StudyJavaUserController {
    @Resource
    private StudyJavaUserService studyJavaUserService;
    // RequestParam 通常用于获取单个参数
    // ModelAttribute 通常用于获取多个参数
    @GetMapping("/getUserList")
    @ResponseBody
    public ResponseResult getUserList(
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @ModelAttribute("studyJavaUser") StudyJavaUser  studyJavaUser) {
        Page<StudyJavaUser> page = new Page<>(pageNum, pageSize);
        IPage<StudyJavaUser> userPage = studyJavaUserService.getUserList(page, studyJavaUser);
        // 返回分页数据和总条数
        Map<String,Object> map = new HashMap<>();
        map.put("data",userPage.getRecords());
        map.put("total",userPage.getTotal());
        return ResponseGenerator.generatSuccessResult(map);
    }
//    @GetMapping("/test")
//    @ResponseBody
//    public Map<String, Object> test(){
//        List<Map<String, Object>> userList = new ArrayList<>();
//        // 创建并添加第一个用户
//        Map<String, Object> user1 = new HashMap<>();
//        user1.put("name", "Alice");
//        user1.put("id", 1);
//        userList.add(user1);
//
//        // 创建并添加第二个用户
//        Map<String, Object> user2 = new HashMap<>();
//        user2.put("name", "Bob");
//        user2.put("id", 2);
//        userList.add(user2);
//
//        // 创建并添加第三个用户
//        Map<String, Object> user3 = new HashMap<>();
//        user3.put("name", "Charlie");
//        user3.put("id", 3);
//        userList.add(user3);
//        Map<String, Object> result = new HashMap<>();
//        result.put("data", userList);
//        result.put("code",200);
//        return result;
//    }
    @PostMapping("/updateUser")
    @ResponseBody
    public ResponseResult updateUser(@RequestBody StudyJavaUser studyJavaUser) {
        // 获取用户ID
        Long userId = studyJavaUser.getUserId();
        if(userId == null){
            return ResponseGenerator.generatErrorResult("用户ID不能为空");
        }
        String nickName = studyJavaUser.getNickName();
        if (!StringUtils.isNoneEmpty(nickName)){
            return  ResponseGenerator.generatErrorResult("昵称不能为空");
        }
        String loginName = studyJavaUser.getLoginName();
        if (!StringUtils.isNoneEmpty(loginName)){
            return  ResponseGenerator.generatErrorResult("登录名不能为空");
        }
        String passwordMd5 = studyJavaUser.getPasswordMd5();
        if (!StringUtils.isNoneEmpty(passwordMd5)){
            return  ResponseGenerator.generatErrorResult("密码不能为空");
        }
        String introduceSign = studyJavaUser.getIntroduceSign();
        if (!StringUtils.isNoneEmpty(introduceSign)){
            return  ResponseGenerator.generatErrorResult("介绍不能为空");
        }
        String address = studyJavaUser.getAddress();
        if (!StringUtils.isNoneEmpty(address)){
            return  ResponseGenerator.generatErrorResult("地址不能为空");
        }
        studyJavaUserService.updateUser(studyJavaUser);
        // 返回更新结果
        return ResponseGenerator.generatSuccessResult(true);
    }
    @PostMapping("/insertUser")
    @ResponseBody
    public ResponseResult insertUser(@RequestBody StudyJavaUser studyJavaUser) {

        studyJavaUserService.insertUser(studyJavaUser);
        // 返回插入结果
        return ResponseGenerator.generatSuccessResult(true);
    }
    @DeleteMapping("/deleteUser")
    @ResponseBody
    public ResponseResult deleteUser(@RequestBody StudyJavaUser studyJavaUser) {
        studyJavaUserService.deleteUser(studyJavaUser);
        // 返回插入结果
        return ResponseGenerator.generatSuccessResult(true);
    }
}
