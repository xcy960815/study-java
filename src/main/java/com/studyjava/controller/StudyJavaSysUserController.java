package com.studyjava.controller;

import com.studyjava.domain.dto.StudyJavaSysUserDto;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.studyjava.domain.vo.StudyJavaSysUserVo;
import com.studyjava.exception.StudyJavaException;
import com.studyjava.utils.PageResult;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import com.studyjava.annotation.Log;
import com.studyjava.domain.enums.BusinessType;
import com.studyjava.service.StudyJavaSysUserService;
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
     * @param pageNum int
     * @param pageSize int
     * @param studyJavaUser StudyJavaSysUserDto
     * @return PageResult<StudyJavaSysUserVo>
     */
    @PostMapping("/getUserList")
    public PageResult<StudyJavaSysUserVo> getUserList(
        @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
        @RequestBody StudyJavaSysUserDto studyJavaSysUserDto
    ) {
        IPage<StudyJavaSysUserVo> userVoPage = studyJavaSysUserService.getUserList(startPage(pageNum, pageSize), studyJavaSysUserDto);
        return PageResult.of(userVoPage.getRecords(), userVoPage.getTotal());
    }

    /**
     * 获取用户信息
     * @return StudyJavaSysUserVo
     */
    @GetMapping("/getUserInfo")
    public StudyJavaSysUserVo getUserInfo(){
        return studyJavaSysUserService.getUserInfo();
    }

    // RequestParam 通常用于获取单个参数
    // ModelAttribute 通常用于获取多个参数
    /**
     * 更新用户
     * @param studyJavaSysUser StudyJavaSysUserDto
     * @return Boolean
     */
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PostMapping("/updateUser")
    public Boolean updateUser(@Valid @RequestBody StudyJavaSysUserDto studyJavaSysUser) {
        // 获取用户ID
        Long userId = studyJavaSysUser.getId();
        if(userId == null){
            throw new StudyJavaException("用户ID不能为空");
        }
        studyJavaSysUserService.updateUser(studyJavaSysUser);
        // 返回更新结果
        return true;
    }

    /**
     * 更新用户头像
     * @param userId String
     * @param file MultipartFile
     * @return String
     */
    @PostMapping("/updateUserAvatar")
    public String updateUserAvatar(
            @RequestParam("userId") String userId,
            @RequestParam("file") MultipartFile file
    ) {
        if(userId == null){
            throw new StudyJavaException("用户ID不能为空");
        }
        try {
           return studyJavaSysUserService.updateUserAvatar(userId,file);
        }catch (IOException error){
            throw new StudyJavaException("更新头像失败" + error.getMessage());
        }
    }

    /**
     * 更新用户信息
     * @param studyJavaUser StudyJavaSysUserDto
     * @return Boolean
     */
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping("/insertUser")
    public Boolean insertUser(@Valid @RequestBody StudyJavaSysUserDto studyJavaUser) {
        // 返回插入结果
        return studyJavaSysUserService.insertUser(studyJavaUser);
    }

    /**
     * 删除用户信息
     * @param studyJavaSysUserDto StudyJavaSysUserDto
     * @return Boolean
     */
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/deleteUser")
    public Boolean deleteUser(@RequestBody StudyJavaSysUserDto studyJavaSysUserDto) {
        // 返回删除结果
        return studyJavaSysUserService.deleteUser(studyJavaSysUserDto);
    }

    /**
     * 更新用户密码
     * @return Boolean
     */
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PostMapping("/updateUserPassword")
    public Boolean updateUserPassword(@RequestBody StudyJavaSysUserDto studyJavaSysUserDto) {
        // 返回插入结果
        return studyJavaSysUserService.updateUserPassword(studyJavaSysUserDto);
    }
}
