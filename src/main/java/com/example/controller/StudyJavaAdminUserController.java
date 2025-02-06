package com.example.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.domain.vo.StudyJavaAdminUserVo;
import com.example.domain.dto.StudyJavaAdminUserDto;
import com.example.exception.StudyJavaException;
import com.example.service.StudyJavaAdminUserService;
import com.example.utils.ResponseGenerator;
import com.example.utils.ResponseResult;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping ("/adminUser")
public class StudyJavaAdminUserController {
    @Resource
    StudyJavaAdminUserService studyJavaAdminUserService;

    @RequestMapping("/getAdminUserList")
    public ResponseResult<Map<String, Object>> getAdminUserList(
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
        return ResponseGenerator.generateSuccessResult(map);
    }

    @PostMapping("/updateUser")
    public ResponseResult<Boolean> updateUser(@Valid @RequestBody StudyJavaAdminUserVo studyJavaAdminUser) {
        Integer adminUserId = studyJavaAdminUser.getAdminUserId();
        if(adminUserId == null){
           throw new StudyJavaException("用户ID不能为空");
        }
        studyJavaAdminUserService.updateAdminUser(studyJavaAdminUser);
        // 返回更新结果
        return ResponseGenerator.generateSuccessResult(true);
    };

    @PostMapping("/insertUser")
    public ResponseResult<Boolean> insertUser(@Valid @RequestBody StudyJavaAdminUserVo studyJavaAdminUser) {
        studyJavaAdminUserService.insertAdminUser(studyJavaAdminUser);
        // 返回插入结果
        return ResponseGenerator.generateSuccessResult(true);
    }

    @DeleteMapping("/deleteUser")
    public ResponseResult<Boolean> deleteUser(@RequestBody StudyJavaAdminUserVo studyJavaAdminUser) {
        Integer adminUserId = studyJavaAdminUser.getAdminUserId();
        if(adminUserId == null){
            throw new StudyJavaException("用户ID不能为空");
        }
        studyJavaAdminUserService.deleteAdminUser(studyJavaAdminUser);
        // 返回插入结果
        return ResponseGenerator.generateSuccessResult(true);
    }
}
