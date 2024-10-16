package com.example.controller;
import com.example.domain.StudyJavaUser;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.example.utils.ResponseResult;
import com.example.utils.ResponseGenerator;
import com.example.service.StudyJavaUserService;

import java.util.List;

@Controller
@RequestMapping("/user")
public class StudyJavaUserController {
    @Resource
    private StudyJavaUserService studyJavaUserService;
    /**
     * 生成成功响应
     * @return ResponseResult
     */
    @RequestMapping("/getAllUser")
    @ResponseBody
    public ResponseResult getSuccess() {
        List<StudyJavaUser> userList = studyJavaUserService.getAllUsers();
        return ResponseGenerator.generatSuccessResult(userList) ;
    }
    /**
     * 生成错误响应
     */
    @RequestMapping("/getError")
    @ResponseBody
    public ResponseResult getError() {
        String helloWordServiceGetError = studyJavaUserService.getError();
        return ResponseGenerator.generatErrorResult(helloWordServiceGetError);
    }
}
