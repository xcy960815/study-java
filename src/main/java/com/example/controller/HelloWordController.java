package com.example.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.example.utils.ResponseResult;
import com.example.utils.ResponseGenerator;



@Controller
@RequestMapping("/root")
public class HelloWordController {
    /**
     * 生成成功响应
     * @return ResponseResult
     */
    @RequestMapping("/getSuccess")
    @ResponseBody
    public ResponseResult helloWord() {
        return ResponseGenerator.generatSuccessResult("我是成功返回") ;
    }
    /**
     * 生成错误响应
     */
    @RequestMapping("/getError")
    @ResponseBody
    public ResponseResult getError() {
        return ResponseGenerator.generatErrorResult("我是错误返回");
    }
}
