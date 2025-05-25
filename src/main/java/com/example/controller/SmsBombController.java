//package com.example.studyjava.controller;
//
//import org.springframework.web.bind.annotation.*;
//import org.springframework.http.ResponseEntity;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Async;
//import java.util.concurrent.CompletableFuture;
//import java.util.Map;
//import java.util.HashMap;
//
//@RestController
//@RequestMapping("/sms")
//public class SmsBombController {
//
//    @PostMapping("/bomb")
//    public ResponseEntity<?> sendSmsBomb(@RequestBody Map<String, String> request) {
//        String phoneNumber = request.get("phoneNumber");
//        int count = Integer.parseInt(request.getOrDefault("count", "10"));
//
//        if (phoneNumber == null || phoneNumber.isEmpty()) {
//            return ResponseEntity.badRequest().body("手机号码不能为空");
//        }
//
//        // 这里应该添加手机号码格式验证
//        if (!phoneNumber.matches("^1[3-9]\\d{9}$")) {
//            return ResponseEntity.badRequest().body("无效的手机号码格式");
//        }
//
//        // 限制最大发送次数
//        if (count > 100) {
//            count = 100;
//        }
//
//        // 异步发送短信
//        CompletableFuture.runAsync(() -> {
//            for (int i = 0; i < count; i++) {
//                try {
//                    // TODO: 实现实际的短信发送逻辑
//                    // sendSms(phoneNumber);
//                    Thread.sleep(1000); // 添加延迟，避免发送过快
//                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt();
//                    break;
//                }
//            }
//        });
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("message", "短信轰炸任务已启动");
//        response.put("phoneNumber", phoneNumber);
//        response.put("count", count);
//
//        return ResponseEntity.ok(response);
//    }
//
//    @GetMapping("/status")
//    public ResponseEntity<?> getBombStatus(@RequestParam String phoneNumber) {
//        // TODO: 实现获取短信发送状态的逻辑
//        Map<String, Object> response = new HashMap<>();
//        response.put("phoneNumber", phoneNumber);
//        response.put("status", "进行中");
//        return ResponseEntity.ok(response);
//    }
//}
