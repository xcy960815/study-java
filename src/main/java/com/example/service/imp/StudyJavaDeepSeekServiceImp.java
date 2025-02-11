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
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StudyJavaDeepSeekServiceImp implements StudyJavaDeepSeekService {
    /**
     * api key
     */
    private static final String DEEPSEEK_API_KEY = "Bearer sk-e3cfe3854fb0448f8033ad926d454baa";
    /**
     * 域名
     */
    private static final String DEEPSEEK_BASE_URL = "https://api.deepseek.com";
    /**
     * completions 接口地址
     */
    private static final String DEEPSEEK_COMPLETIONS_URL = "/v1/chat/completions";

    /**
     * 构建请求地址
     * @param url String
     * @return String
     */
    private static String buildRequestUrl(@NonNull String url){
        return DEEPSEEK_BASE_URL + url;
    };


    @Override
    public HttpResponse completions() {
        try {
            // 创建请求对象
            StudyJavaDeepSeekVo studyJavaDeepseekVo = new StudyJavaDeepSeekVo();

            studyJavaDeepseekVo.setModel("deepseek-r1:32b");

            List<Map<String, String>> messages = new ArrayList<>();

            messages.add(new HashMap<String, String>() {{
                put("role", "user");
                put("content", "你好");
            }});

            studyJavaDeepseekVo.setMessages(messages);

            studyJavaDeepseekVo.setStream(true);

            // 发送请求，返回 HttpResponse
            return HttpRequest.post(buildRequestUrl(DEEPSEEK_COMPLETIONS_URL))
                    .header("Authorization", DEEPSEEK_API_KEY)
                    .header("Content-Type", "application/json")
                    .body(JsonUtils.toJson(studyJavaDeepseekVo))
                    .execute();

        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
        catch (Exception e) {
            throw new StudyJavaException("请求失败");
        }
    }

}

