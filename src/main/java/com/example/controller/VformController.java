package com.example.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import cn.hutool.core.date.DateUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/vform")
public class VformController {

    @GetMapping("/test")
    @ResponseBody
    public Map<String, Object> test(HttpServletRequest request){
        int clientPort =  request.getRemotePort();
        String clientAddr = request.getRemoteAddr();
        int serverPort = request.getLocalPort();
        String serverAddr = request.getLocalAddr();
        String now = DateUtil.now();
//        System.out.println(clientPort + " " + clientAddr + " " + serverPort + " " + serverAddr + " " + now);
        log.info( clientPort + " " + clientAddr + " " + serverPort + " " + serverAddr + " " + now);
        List<Map<String, Object>> userList = new ArrayList<>();
        // 创建并添加第一个用户
        Map<String, Object> user1 = new HashMap<>();
        user1.put("name", "Alice");
        user1.put("id", "1");
        userList.add(user1);

        // 创建并添加第二个用户
        Map<String, Object> user2 = new HashMap<>();
        user2.put("name", "Bob");
        user2.put("id", "2");
        userList.add(user2);

        // 创建并添加第三个用户
        Map<String, Object> user3 = new HashMap<>();
        user3.put("name", "Charlie");
        user3.put("id", "3");
        userList.add(user3);
        Map<String, Object> result = new HashMap<>();
        result.put("data", userList);
        result.put("code",200);
        return result;
    }
    @GetMapping("/test1")
    @ResponseBody
    public Map<String, Object> test1(@RequestParam(value = "name",defaultValue = "") String name){
        List<Map<String, Object>> userList = new ArrayList<>();
        // 创建并添加第一个用户
        Map<String, Object> user1 = new HashMap<>();
        user1.put("name", "Alice");
        user1.put("id", "1");
        userList.add(user1);

        // 创建并添加第二个用户
        Map<String, Object> user2 = new HashMap<>();
        user2.put("name", "Bob");
        user2.put("id", "2");
        userList.add(user2);

        // 创建并添加第三个用户
        Map<String, Object> user3 = new HashMap<>();
        user3.put("name", "Charlie");
        user3.put("id", "3");
        userList.add(user3);
        Map<String, Object> result = new HashMap<>();

        List<Map<String, Object>> resultList = new ArrayList<>();

        for (Map<String, Object> user : userList) {
            if (name == null || name.isEmpty()) {
                resultList.add(user);
            } else {
                if (name.equals(user.get("name"))) {
                    resultList.add(user);
                }
            }
        }
        result.put("data", resultList);
        result.put("code",200);
        return result;
    }

    @GetMapping("/test2")
    @ResponseBody
    public Map<String, Object> test2(@RequestParam(value = "name",defaultValue = "") String name){
        List<Map<String, Object>> userList = new ArrayList<>();
        // 创建并添加第一个用户
        Map<String, Object> user1 = new HashMap<>();
        user1.put("name", "Alice");
        user1.put("id", "1");
        userList.add(user1);

        // 创建并添加第二个用户
        Map<String, Object> user2 = new HashMap<>();
        user2.put("name", "Bob");
        user2.put("id", "2");
        userList.add(user2);

        // 创建并添加第三个用户
        Map<String, Object> user3 = new HashMap<>();
        user3.put("name", "Charlie");
        user3.put("id", "3");
        userList.add(user3);
        Map<String, Object> result = new HashMap<>();

        List<Map<String, Object>> resultList = new ArrayList<>();

        for (Map<String, Object> user : userList) {
            if (name == null || name.isEmpty()) {
                resultList.add(user);
            } else {
                if (name.equals(user.get("name"))) {
                    resultList.add(user);
                }
            }
        }
        result.put("data", resultList);
        result.put("code",200);
        return result;
    }


}
