package com.example.service.imp;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.example.domain.vo.StudyJavaDeepSeekVo;
import com.example.exception.StudyJavaException;
import com.example.service.StudyJavaDeepSeekService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StudyJavaDeepSeekServiceImp implements StudyJavaDeepSeekService {
    private static final String DEEPSEEK_API_URL = "https://api.deepseek.com/v1/chat/completions";
    private static final String DEEPSEEK_API_KEY = "Bearer sk-e3cfe3854fb0448f8033ad926d454baa";


    @Override
    public HttpResponse completions() {
        try {
            // 创建请求对象
            StudyJavaDeepSeekVo studyJavaDeepSeekVo = new StudyJavaDeepSeekVo();
            studyJavaDeepSeekVo.setModel("deepseek-chat");
            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(new HashMap<String, String>() {{
                put("role", "user");
                put("content", "你好");
            }});
            studyJavaDeepSeekVo.setMessages(messages);
            studyJavaDeepSeekVo.setStream(true); // 开启流式输出

            // 发送请求，返回 HttpResponse
            HttpResponse response = HttpRequest.post(DEEPSEEK_API_URL)
                    .header("Authorization", DEEPSEEK_API_KEY)
                    .header("Content-Type", "application/json")
                    .body(JsonUtils.toJson(studyJavaDeepSeekVo))
                    .execute();

            return response; // 直接返回响应

        } catch (Exception e) {
            e.printStackTrace();
            throw new StudyJavaException("请求失败");
        }
    }

}

