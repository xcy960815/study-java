package com.studyjava.service.impl;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import com.studyjava.domain.dto.ollama.*;
import com.studyjava.domain.vo.ollama.*;
import com.studyjava.exception.StudyJavaException;
//import com.example.exception.StudyJavaAiException;
import com.studyjava.service.StudyJavaAiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import com.studyjava.service.StudyJavaOllamaService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.io.*;
import java.net.http.*;
import java.net.http.HttpRequest.Builder;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Ollama服务实现类
 */
@Slf4j
@Service
public class StudyJavaOllamaServiceImpl extends StudyJavaAiService implements StudyJavaOllamaService  {
    /**
     * 端口号
     */
    private static final Integer Ollama_Port = 11434;

    /**
     * 域名
     */
    private static final String Ollama_Domain = "http://localhost";

    /**
     * completions 接口
     */
    private static final String Ollama_Completions_Api = "/v1/chat/completions";

    /**
     * tags 接口
     */
    private static final String Ollama_Tags_Api = "/api/tags";

    /**
     * models 接口
     */
    private static final String Ollama_Models_Api = "/v1/models";

    /**
     * 删除模型接口
     */
    private static final String Ollama_Delete_Model_Api = "/api/delete";

    /**
     * 列出运行模型
     */
    private static final String Ollama_Ps_Api = "/api/ps";

    /**
     * 拉取模型
     */
    private static final String Ollama_Pull_Api = "/api/pull";

    /**
     * 获取模型详情
     */
    private static final String Ollama_Show_Api = "/api/show";

    /**
     * 超时时间 10 分钟
     */
    private static final int Ollama_Timeout = 60 * 1000 * 10;

    /**
     * version 接口
     */
    private static final String Ollama_Version_Api = "/api/version";



    /**
     * 构建请求地址
     * @param url String
     * @return String
     */
    private URI generateRequestUrl(String url){
        return URI.create(Ollama_Domain + ":" + Ollama_Port + url);
    }

    /**
     * 构建请求头
     * @param uri URI
     * @return Builder
     */
    private Builder generateRequestBuilder(URI uri) {
        return HttpRequest.newBuilder(uri)
                .header("Content-Type", "application/json")
                .timeout(Duration.ofMillis(Ollama_Timeout));
    }

    /**
     * 获取所有的模型
     */
    @Override
    public StudyJavaOllamaModelsDto models() throws IOException, InterruptedException {
        HttpRequest httpRequest = generateRequestBuilder(generateRequestUrl(Ollama_Models_Api))
                .GET()
                .build();
        // 发送请求并获取响应
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return handleResponse(response, StudyJavaOllamaModelsDto.class);
    }

    /**
     * 列出ollama本地模型
     * @return StudyJavaOllamaTagsDto
     */
    @Override
    public StudyJavaOllamaTagsDto tags() throws IOException, InterruptedException {
        // 构建请求
        HttpRequest httpRequest = generateRequestBuilder(generateRequestUrl(Ollama_Tags_Api))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return handleResponse(response, StudyJavaOllamaTagsDto.class);
    }

    /**
     * 获取本地ollama的版本
     * @return StudyJavaOllamaVersionDto
     */
    @Override
    public StudyJavaOllamaVersionDto version() throws IOException, InterruptedException {
        HttpRequest httpRequest = generateRequestBuilder(generateRequestUrl(Ollama_Version_Api))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return handleResponse(response, StudyJavaOllamaVersionDto.class);
    }
    /**
     * 获取加载到内存中的模型
     */
    @Override
    public StudyJavaOllamaPsDto ps() throws IOException, InterruptedException {
        HttpRequest httpRequest = generateRequestBuilder(generateRequestUrl(Ollama_Ps_Api))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return handleResponse(response, StudyJavaOllamaPsDto.class);
    }

    /**
     * 获取模型详情
     */
    @Override
    public StudyJavaOllamaShowDto show(StudyJavaOllamaShowVo studyJavaOllamaShowVo) throws IOException, InterruptedException {
        HttpRequest httpRequest = generateRequestBuilder(generateRequestUrl(Ollama_Show_Api))
                .POST(studyJavaOllamaShowVo.getBodyPublisher())
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return handleResponse(response, StudyJavaOllamaShowDto.class);
    }
    /**
     * 删除指定的模型
     * @param studyJavaOllamaDeleteVo StudyJavaOllamaDeleteVo
     * @return Boolean
     */
    @Override
    public void delete(StudyJavaOllamaDeleteVo studyJavaOllamaDeleteVo) throws IOException, InterruptedException {
        HttpRequest httpRequest = generateRequestBuilder(generateRequestUrl(Ollama_Delete_Model_Api))
                .method("DELETE", studyJavaOllamaDeleteVo.getBodyPublisher())
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        log.info("response--response {}",response);
        if(response.statusCode() != 200) {
             throw new StudyJavaException("请求失败");
        }
    }
    /**
     * 会话接口
     * @param studyJavaOllamaCompletionsVo StudyJavaOllamaCompletionsVo
     * @param emitter SseEmitter
     */
    @Override
    public void completions(StudyJavaOllamaCompletionsVo studyJavaOllamaCompletionsVo, SseEmitter emitter) {
        CompletableFuture.runAsync(() -> {
            try {
                HttpRequest httpRequest = generateRequestBuilder(generateRequestUrl(Ollama_Completions_Api))
                        .POST(studyJavaOllamaCompletionsVo.getBodyPublisher())
                        .build();
                HttpResponse<InputStream> response = httpClient.send(httpRequest, BodyHandlers.ofInputStream());

                int statusCode = response.statusCode();
                if (statusCode != 200) {
                    try {
                        emitter.completeWithError(new StudyJavaException("请求失败，状态码：" + statusCode));
                    } catch (Exception e) {
                        log.info("发送错误信号时连接已断开");
                    }
                    return;
                }

                handleStreamResponse(response, emitter);
            } catch (IOException | InterruptedException e) {
                log.error("Error during HTTP request: {}", e.getMessage());
                try {
                    emitter.completeWithError(e);
                } catch (Exception ex) {
                    log.info("发送错误信号时连接已断开");
                }
                Thread.currentThread().interrupt(); // 保持中断状态
            }
        });
    }
//    TODO 拉取模型 提示 404 page not found
    @Override
    public void pull(StudyJavaOllamaPullVo studyJavaOllamaPullVo) {
        try {
            HttpRequest httpRequest = generateRequestBuilder(generateRequestUrl(Ollama_Pull_Api))
                    .POST(studyJavaOllamaPullVo.getBodyPublisher())
                    .build();
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new StudyJavaException("请求失败: " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw new StudyJavaException("请求失败: " + e.getMessage());
        }
    }

    /**
     * 处理流式响应
     */
    private void handleStreamResponse(HttpResponse<InputStream> response, SseEmitter emitter) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.body(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // 检查客户端连接状态 - SseEmitter没有isCompleted方法，我们通过try-catch来处理
                try {
                    emitter.send(line);
                } catch (IOException e) {
                    // 捕获发送失败异常，通常是客户端断开连接
                    if (e.getCause() instanceof java.io.IOException &&
                        e.getCause().getMessage().contains("Broken pipe")) {
                        log.info("客户端连接已断开，停止发送数据");
                        return; // 直接返回，不继续处理
                    }
                    throw e;
                }
            }
            // 只有在连接仍然活跃时才完成
            try {
                emitter.complete();
            } catch (Exception e) {
                log.info("发送完成信号时连接已断开");
            }
        } catch (IOException e) {
            log.error("处理流式响应失败", e);
            // 只有在连接仍然活跃时才发送错误
            try {
                emitter.completeWithError(e);
            } catch (Exception ex) {
                log.info("发送错误信号时连接已断开");
            }
        }
    }

    /**
     * 关闭资源
     */
    public void shutdown() {
        // executorService was removed, so this method is no longer needed for that purpose
        // If there are other resources to shut down, they should be added here.
        // For now, it can be left empty or removed if no other resources require explicit shutdown.
    }
}
