package com.example.service.imp;
import com.example.service.HelloWordService;
import org.springframework.stereotype.Service;


@Service
public class HelloWordServiceImp implements HelloWordService {
    public String getSuccess() {
        return "我是 HelloWordServer 类的成功返回";
    }

    public String getError(){
        return "我是 HelloWordServer 类的错误返回";
    }
}
