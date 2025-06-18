package com.example.controller;

import com.example.domain.dto.StudyJavaSysUserDto;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.domain.vo.StudyJavaSysUserVo;
import com.example.exception.StudyJavaException;
import com.example.utils.ResponseListResult;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import com.example.utils.ResponseResult;
import com.example.utils.ResponseGenerator;
import com.example.service.StudyJavaSysUserService;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/user")
public class StudyJavaSysUserController extends BaseController {

    @Resource
    private StudyJavaSysUserService studyJavaSysUserService;

    /**
     * 获取用户列表
     * @param pageSize int
     * @param pageNum int
     * @param studyJavaUser StudyJavaSysUserDto
     * @return ResponseResult<Map<String,Object>>
     */
    @GetMapping("/getUserList")
    public ResponseListResult<StudyJavaSysUserVo> getUserList(
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @ModelAttribute("studyJavaUser") StudyJavaSysUserDto studyJavaUser
    ) {
        IPage<StudyJavaSysUserVo> userVoPage = studyJavaSysUserService.getUserList(startPage(pageNum, pageSize), studyJavaUser);
        return ResponseGenerator.generateListResult(userVoPage.getRecords(),userVoPage.getTotal());
    }

    /**
     * 获取用户信息
     * @return ResponseResult<StudyJavaSysUserVo>
     */
    @GetMapping("/getUserInfo")
    public ResponseResult<StudyJavaSysUserVo> getUserInfo(){
        return ResponseGenerator.generateSuccessResult(studyJavaSysUserService.getUserInfo());
    }

    // RequestParam 通常用于获取单个参数
    // ModelAttribute 通常用于获取多个参数
    /**
     * 更新用户
     * @param studyJavaSysUser StudyJavaSysUserDto
     * @return ResponseResult<Boolean>
     */
    @PostMapping("/updateUserInfo")
    public ResponseResult<Boolean> updateUserInfo(@Valid @RequestBody StudyJavaSysUserDto studyJavaSysUser) {
        // 获取用户ID
        Long userId = studyJavaSysUser.getId();
        if(userId == null){
            throw new StudyJavaException("用户ID不能为空");
        }
        studyJavaSysUserService.updateUserInfo(studyJavaSysUser);
        // 返回更新结果
        return ResponseGenerator.generateSuccessResult(true);
    }

    /**
     * 更新用户头像
     * @param userId String
     * @param file MultipartFile
     * @return ResponseResult<String>
     */
    @PostMapping("/updateUserAvatar")
    public ResponseResult<String> updateUserAvatar(
            @RequestParam("userId") String userId,
            @RequestParam("file") MultipartFile file
    ) {
        if(userId == null){
            throw new StudyJavaException("用户ID不能为空");
        }
        try {
           String base64Image = studyJavaSysUserService.updateUserAvatar(userId,file);
            // 返回更新结果
            return ResponseGenerator.generateSuccessResult(base64Image);
        }catch (IOException error){
            throw new StudyJavaException("更新头像失败" + error.getMessage());
        }
    }

    /**
     * 更新用户信息
     * @param studyJavaUser StudyJavaSysUserDto
     * @return ResponseResult<Boolean>
     */
    @PostMapping("/insertUserInfo")
    public ResponseResult<Boolean> insertUserInfo(@Valid @RequestBody StudyJavaSysUserDto studyJavaUser) {
        // 返回插入结果
        return ResponseGenerator.generateSuccessResult(studyJavaSysUserService.insertUserInfo(studyJavaUser));
    }

    /**
     * 删除用户信息
     * @param studyJavaUser StudyJavaSysUserDto
     * @return ResponseResult<Boolean>
     */
    @DeleteMapping("/deleteUserInfo")
    public ResponseResult<Boolean> deleteUserInfo(@RequestBody StudyJavaSysUserDto studyJavaUser) {
        // 返回插入结果
        return ResponseGenerator.generateSuccessResult(studyJavaSysUserService.deleteUserInfo(studyJavaUser));
    }

    /**
     * 更新用户密码
     * @return ResponseResult<Boolean>
     */
    @PostMapping("/updateUserPassword")
    public ResponseResult<Boolean> updateUserPassword(@RequestBody StudyJavaSysUserDto studyJavaSysUserDto) {
        // 返回插入结果
        return ResponseGenerator.generateSuccessResult(studyJavaSysUserService.updateUserPassword(studyJavaSysUserDto));
    }
}
