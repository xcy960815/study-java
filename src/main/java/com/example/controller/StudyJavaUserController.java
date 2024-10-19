package com.example.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.domain.StudyJavaUser;
import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.utils.ResponseResult;
import com.example.utils.ResponseGenerator;
import com.example.service.StudyJavaUserService;

import java.util.HashMap;
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
    @PostMapping("/updateUser")
    @ResponseBody
    public ResponseResult updateUser(@RequestBody StudyJavaUser studyJavaUser) {
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
}
