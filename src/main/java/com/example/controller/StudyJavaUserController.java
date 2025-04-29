package com.example.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.domain.vo.StudyJavaUserVo;
import com.example.domain.dto.StudyJavaUserDto;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.exception.StudyJavaException;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import com.example.utils.ResponseResult;
import com.example.utils.ResponseGenerator;
import com.example.service.StudyJavaUserService;
import com.example.component.JwtTokenComponent;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class StudyJavaUserController extends BaseController {

    @Resource
    private JwtTokenComponent jwtTokenComponent;
    @Resource
    private StudyJavaUserService studyJavaUserService;

    /**
     * 获取用户列表
     * @param pageSize int
     * @param pageNum int
     * @param studyJavaUser StudyJavaUserVo
     * @return ResponseResult<Map<String,Object>>
     */
    @GetMapping("/getUserList")
    public ResponseResult<Map<String,Object>> getUserList(
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @ModelAttribute("studyJavaUser") StudyJavaUserVo  studyJavaUser
    ) {
        IPage<StudyJavaUserDto> userPage = studyJavaUserService.getUserList(startPage(pageNum, pageSize), studyJavaUser);
        return ResponseGenerator.generateSuccessResult(getPageData(userPage));
    }

    /**
     * 获取用户信息
     * @param authorization String
     * @return ResponseResult<StudyJavaUserDto>
     */
    @GetMapping("/getUserInfo")
    public ResponseResult<StudyJavaUserDto> getUserInfo(@RequestHeader(value = "Authorization", required = false) String authorization){
        JSONObject tokenUserInfo = JSONUtil.parseObj(jwtTokenComponent.getUserInfoFromAuthorization(authorization));
        Long userId = Long.parseLong(tokenUserInfo.get("userId").toString());
        String loginName = tokenUserInfo.get("loginName").toString();
        StudyJavaUserVo studyJavaUserVo = new StudyJavaUserVo();
        studyJavaUserVo.setUserId(userId);
        studyJavaUserVo.setLoginName(loginName);
        StudyJavaUserDto userInfo = studyJavaUserService.getUserInfo(studyJavaUserVo);
        return ResponseGenerator.generateSuccessResult(userInfo);
    }

    // RequestParam 通常用于获取单个参数
    // ModelAttribute 通常用于获取多个参数

    /**
     * 更新用户
     * @param studyJavaUser StudyJavaUserVo
     * @return ResponseResult<Boolean>
     */
    @PostMapping("/updateUserInfo")
    public ResponseResult<Boolean> updateUserInfo(@Valid @RequestBody StudyJavaUserVo studyJavaUser) {
        // 获取用户ID
        Long userId = studyJavaUser.getUserId();
        if(userId == null){
            throw new StudyJavaException("用户ID不能为空");
        }
        studyJavaUserService.updateUserInfo(studyJavaUser);
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
           String base64Image = studyJavaUserService.updateUserAvatar(userId,file);
            // 返回更新结果
            return ResponseGenerator.generateSuccessResult(base64Image);
        }catch (IOException error){
            throw new StudyJavaException("更新头像失败" + error.getMessage());
        }
    }

    /**
     * 更新用户信息
     * @param studyJavaUser StudyJavaUserVo
     * @return ResponseResult<Boolean>
     */
    @PostMapping("/insertUserInfo")
    public ResponseResult<Boolean> insertUserInfo(@Valid @RequestBody StudyJavaUserVo studyJavaUser) {
        studyJavaUserService.insertUserInfo(studyJavaUser);
        // 返回插入结果
        return ResponseGenerator.generateSuccessResult(true);
    }

    /**
     * 删除用户信息
     * @param studyJavaUser StudyJavaUserVo
     * @return ResponseResult<Boolean>
     */
    @DeleteMapping("/deleteUserInfo")
    public ResponseResult<Boolean> deleteUserInfo(@RequestBody StudyJavaUserVo studyJavaUser) {
        studyJavaUserService.deleteUserInfo(studyJavaUser);
        // 返回插入结果
        return ResponseGenerator.generateSuccessResult(true);
    }

    /**
     * 更新用户密码
     * @param authorization String
     * @param studyJavaUser StudyJavaUserVo
     * @return ResponseResult<Boolean>
     */
    @PostMapping("/updateUserPassword")
    public ResponseResult<Boolean> updateUserPassword(@RequestHeader(value = "Authorization", required = false) String authorization,@RequestBody StudyJavaUserVo studyJavaUser) {
        String token = authorization.substring(7);
        String userInfoStr = jwtTokenComponent.getUserInfoFromToken(token);
        String[] userInfoArr = userInfoStr.split(":");
        Long userId = Long.parseLong(userInfoArr[0]);
        String loginName = userInfoArr[1];
        studyJavaUser.setUserId(userId);
        studyJavaUser.setLoginName(loginName);
        studyJavaUserService.updateUserPassword(studyJavaUser);
        // 返回插入结果
        return ResponseGenerator.generateSuccessResult(true);
    }
}
