package com.example.controller;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.example.utils.ResponseResult;
import com.example.utils.ResponseGenerator;
import  com.example.service.HelloWordService;

@Controller
@RequestMapping("/root")
public class HelloWordController {
    @Resource
    private HelloWordService helloWordService;
    /**
     * 生成成功响应
     * @return ResponseResult
     */
    @RequestMapping("/getSuccess")
    @ResponseBody
    public ResponseResult getSuccess() {
        String helloWordServiceGetSuccess = helloWordService.getSuccess();
        return ResponseGenerator.generatSuccessResult(helloWordServiceGetSuccess) ;
    }
    /**
     * 生成错误响应
     */
    @RequestMapping("/getError")
    @ResponseBody
    public ResponseResult getError() {
        String helloWordServiceGetError = helloWordService.getError();
        return ResponseGenerator.generatErrorResult(helloWordServiceGetError);
    }
}
