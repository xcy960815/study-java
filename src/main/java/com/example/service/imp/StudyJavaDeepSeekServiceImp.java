package com.example.service.imp;

//import cn.hutool.core.convert.ConvertException;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;
import com.example.exception.StudyJavaException;
import com.example.service.StudyJavaDeepSeekService;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import cn.hutool.json.JSONUtil;
import com.example.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StudyJavaDeepSeekServiceImp implements StudyJavaDeepSeekService {
    private static final String DEEPSEEK_API_URL = "https://api.deepseek.com/v1/chat/completions";
    private static final String DEEPSEEK_API_KEY = "Bearer sk-e3cfe3854fb0448f8033ad926d454baa";

    public static void main(String[] args) {
        StudyJavaDeepSeekServiceImp studyJavaDeepSeekServiceImp = new StudyJavaDeepSeekServiceImp();
        studyJavaDeepSeekServiceImp.query();
    }

    @Override
    public String query() {
        StringBuilder responseContent = new StringBuilder();
        try {
            Map<String, Object> requestParams = new HashMap<>();
            requestParams.put("model", "deepseek-chat");
            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(new HashMap<String, String>() {{
                put("role", "user");
                put("content", "你好");
            }});
            requestParams.put("messages", messages);
            requestParams.put("stream", true); // 开启流式输出

            HttpResponse response = HttpRequest.post(DEEPSEEK_API_URL)
                    .header("Authorization", DEEPSEEK_API_KEY)
                    .header("Content-Type", "application/json")
                    .body(JsonUtils.toJson(requestParams))
                    .execute();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.bodyStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) { // 逐行读取流
                    if (!line.trim().isEmpty()) {
                        try {
                            JSONObject jsonObject = JSONUtil.parseObj(line); // 解析流式 JSON
                            JSONArray choices = jsonObject.getJSONArray("choices");
                            if (choices != null && !choices.isEmpty()) {
                                JSONObject result = choices.get(0, JSONObject.class, Boolean.TRUE);
                                JSONObject message = result.getJSONObject("message");
                                String content = message.getStr("content");
                                log.info("流式输出内容: " + content);
                                responseContent.append(content); // 追加内容
                            }
                        } catch (JSONException e) {
                            log.warn("解析流数据失败: " + line); // 避免解析失败时抛异常
                        }
                    }
                }
            }
        } catch (HttpException e) {
            throw new StudyJavaException("请求失败");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseContent.toString(); // 返回完整流式输出内容
    }

}

