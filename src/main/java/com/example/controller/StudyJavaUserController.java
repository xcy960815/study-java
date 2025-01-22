package com.example.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.domain.vo.StudyJavaUserVo;
import com.example.domain.dto.StudyJavaUserDto;
import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import com.example.utils.ResponseResult;
import com.example.utils.ResponseGenerator;
import com.example.service.StudyJavaUserService;
import com.example.utils.JwtTokenUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user") // 前缀
public class StudyJavaUserController {
    @Resource
    private StudyJavaUserService studyJavaUserService;

    // 获取用户信息
    @GetMapping("/getUserInfo")
    public ResponseResult getUserInfo(@RequestHeader(value = "Authorization", required = false) String authorization){
        String token = authorization.substring(7);
        String userInfoStr = JwtTokenUtil.getUserInfoFromToken(token);
        String[] userInfoArr = userInfoStr.split(":");
        Long userId = Long.parseLong(userInfoArr[0]);
        String loginName = userInfoArr[1];
        StudyJavaUserVo studyJavaUserVo = new StudyJavaUserVo();
        studyJavaUserVo.setUserId(userId);
        studyJavaUserVo.setLoginName(loginName);
        StudyJavaUserDto userInfo = studyJavaUserService.getUserInfo(studyJavaUserVo);
        return ResponseGenerator.generatSuccessResult(userInfo);
    };
    // RequestParam 通常用于获取单个参数
    // ModelAttribute 通常用于获取多个参数
    @GetMapping("/getUserList")
    @ResponseBody
    public ResponseResult getUserList(
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @ModelAttribute("studyJavaUser") StudyJavaUserVo  studyJavaUser
    ) {
        Page<StudyJavaUserVo> page = new Page<>(pageNum, pageSize);
        IPage<StudyJavaUserDto> userPage = studyJavaUserService.getUserList(page, studyJavaUser);
        // 返回分页数据和总条数
        Map<String,Object> map = new HashMap<>();
        map.put("data",userPage.getRecords());
        map.put("total",userPage.getTotal());
        return ResponseGenerator.generatSuccessResult(map);
    }


    @GetMapping("/test")
    @ResponseBody
    public Map<String, Object> test(){
        List<Map<String, Object>> userList = new ArrayList<>();
        // 创建并添加第一个用户
        Map<String, Object> user1 = new HashMap<>();
        user1.put("name", "Alice");
        user1.put("id", "1");
        userList.add(user1);

        // 创建并添加第二个用户
        Map<String, Object> user2 = new HashMap<>();
        user2.put("name", "Bob");
        user2.put("id", "2");
        userList.add(user2);

        // 创建并添加第三个用户
        Map<String, Object> user3 = new HashMap<>();
        user3.put("name", "Charlie");
        user3.put("id", "3");
        userList.add(user3);
        Map<String, Object> result = new HashMap<>();
        result.put("data", userList);
        result.put("code",200);
        return result;
    }
    @GetMapping("/test1")
    @ResponseBody
    public Map<String, Object> test1(@RequestParam(value = "name",defaultValue = "") String name){
        List<Map<String, Object>> userList = new ArrayList<>();
        // 创建并添加第一个用户
        Map<String, Object> user1 = new HashMap<>();
        user1.put("name", "Alice");
        user1.put("id", "1");
        userList.add(user1);

        // 创建并添加第二个用户
        Map<String, Object> user2 = new HashMap<>();
        user2.put("name", "Bob");
        user2.put("id", "2");
        userList.add(user2);

        // 创建并添加第三个用户
        Map<String, Object> user3 = new HashMap<>();
        user3.put("name", "Charlie");
        user3.put("id", "3");
        userList.add(user3);
        Map<String, Object> result = new HashMap<>();

        List<Map<String, Object>> resultList = new ArrayList<>();

        for (Map<String, Object> user : userList) {
            if (name == null || name.isEmpty()) {
                resultList.add(user);
            } else {
                if (name.equals(user.get("name"))) {
                    resultList.add(user);
                }
            }
        }
        result.put("data", resultList);
        result.put("code",200);
        return result;
    }

    @GetMapping("/test2")
    @ResponseBody
    public Map<String, Object> test2(@RequestParam(value = "name",defaultValue = "") String name){
        List<Map<String, Object>> userList = new ArrayList<>();
        // 创建并添加第一个用户
        Map<String, Object> user1 = new HashMap<>();
        user1.put("name", "Alice");
        user1.put("id", "1");
        userList.add(user1);

        // 创建并添加第二个用户
        Map<String, Object> user2 = new HashMap<>();
        user2.put("name", "Bob");
        user2.put("id", "2");
        userList.add(user2);

        // 创建并添加第三个用户
        Map<String, Object> user3 = new HashMap<>();
        user3.put("name", "Charlie");
        user3.put("id", "3");
        userList.add(user3);
        Map<String, Object> result = new HashMap<>();

        List<Map<String, Object>> resultList = new ArrayList<>();

        for (Map<String, Object> user : userList) {
            if (name == null || name.isEmpty()) {
                resultList.add(user);
            } else {
                if (name.equals(user.get("name"))) {
                    resultList.add(user);
                }
            }
        }
        result.put("data", resultList);
        result.put("code",200);
        return result;
    }

    @PostMapping("/updateUserInfo")
    @ResponseBody
    public ResponseResult updateUserInfo(@RequestBody StudyJavaUserVo studyJavaUser) {
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
        studyJavaUserService.updateUserInfo(studyJavaUser);
        // 返回更新结果
        return ResponseGenerator.generatSuccessResult(true);
    }
    @PostMapping("/updateUserAvatar")
    @ResponseBody
    public ResponseResult updateUserAvatar(
            @RequestParam("userId") String userId,
            @RequestParam("file") MultipartFile file
    ) {
        if(userId == null){
            return ResponseGenerator.generatErrorResult("用户ID不能为空");
        }
        try {
           String base64Image = studyJavaUserService.updateUserAvatar(userId,file);

            // 返回更新结果
            return ResponseGenerator.generatSuccessResult(base64Image);
        }catch (IOException error){
            return ResponseGenerator.generatErrorResult("更新头像失败");
        }
    }
    @PostMapping("/insertUserInfo")
    @ResponseBody
    public ResponseResult insertUserInfo(@RequestBody StudyJavaUserVo studyJavaUser) {
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
        studyJavaUserService.insertUserInfo(studyJavaUser);
        // 返回插入结果
        return ResponseGenerator.generatSuccessResult(true);
    }
    @DeleteMapping("/deleteUserInfo")
    @ResponseBody
    public ResponseResult deleteUserInfo(@RequestBody StudyJavaUserVo studyJavaUser) {
        studyJavaUserService.deleteUserInfo(studyJavaUser);
        // 返回插入结果
        return ResponseGenerator.generatSuccessResult(true);
    }

    @PostMapping("/updateUserPassword")
    @ResponseBody
    public ResponseResult updateUserPassword(@RequestHeader(value = "Authorization", required = false) String authorization,@RequestBody StudyJavaUserVo studyJavaUser) {
        String token = authorization.substring(7);
        String userInfoStr = JwtTokenUtil.getUserInfoFromToken(token);
        String[] userInfoArr = userInfoStr.split(":");
        Long userId = Long.parseLong(userInfoArr[0]);
        String loginName = userInfoArr[1];
        studyJavaUser.setUserId(userId);
        studyJavaUser.setLoginName(loginName);
        studyJavaUserService.updateUserPassword(studyJavaUser);
        // 返回插入结果
        return ResponseGenerator.generatSuccessResult(true);
    }
}
