package com.example.controller;



import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/user")
public class StudyJavaUserController {
    @Resource
    private JwtTokenComponent jwtTokenComponent;
    @Resource
    private StudyJavaUserService studyJavaUserService;
    // 获取用户信息
    @GetMapping("/getUserInfo")
    public ResponseResult<StudyJavaUserDto> getUserInfo(@RequestHeader(value = "Authorization", required = false) String authorization){
        String token = authorization.substring(7);
        JSONObject tokenUserInfo = JSONUtil.parseObj(jwtTokenComponent.getUserInfoFromToken(token));
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
    @GetMapping("/getUserList")
    public ResponseResult<Map<String,Object>> getUserList(
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
        return ResponseGenerator.generateSuccessResult(map);
    }

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
            return ResponseGenerator.generateErrorResult("更新头像失败");
        }
    }
    @PostMapping("/insertUserInfo")
    public ResponseResult<Boolean> insertUserInfo(@Valid @RequestBody StudyJavaUserVo studyJavaUser) {
        studyJavaUserService.insertUserInfo(studyJavaUser);
        // 返回插入结果
        return ResponseGenerator.generateSuccessResult(true);
    }
    @DeleteMapping("/deleteUserInfo")
    public ResponseResult<Boolean> deleteUserInfo(@RequestBody StudyJavaUserVo studyJavaUser) {
        studyJavaUserService.deleteUserInfo(studyJavaUser);
        // 返回插入结果
        return ResponseGenerator.generateSuccessResult(true);
    }

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
