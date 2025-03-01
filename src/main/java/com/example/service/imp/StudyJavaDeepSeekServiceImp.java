package com.example.service.imp;

import com.example.domain.dto.deepseek.StudyJavaDeepSeekModelsDto;
import com.example.domain.dto.ollama.StudyJavaOllamaModelsDto;
import com.example.domain.vo.deeseek.StudyJavaDeepSeekCompletionsVo;
import com.example.exception.StudyJavaException;
import com.example.service.StudyJavaDeepSeekService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
public class StudyJavaDeepSeekServiceImp implements StudyJavaDeepSeekService {
    /**
     * api key
     */
    private static final String DEEPSEEK_API_KEY = "Bearer sk-1557b40ba18a42eea27ddeb8a2039413";
    /**
     * 域名
     */
    private static final String DEEPSEEK_BASE_URL = "https://api.deepseek.com";
    /**
     * completions 接口地址
     */
    private static final String DEEPSEEK_COMPLETIONS_URL = "/v1/chat/completions";


    /**
     * models
     */
    private static final String DEEPSEEK_MODELS_URL = "/v1/models";

    /**
     * 超时时间 10 分钟
     */
    private static final int DeepSeek_Timeout = 60 * 1000 * 10;

    /**
     * http 客户端
     */
    private final HttpClient httpClient = HttpClient.newHttpClient();

    /**
     * 线程池
     */
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    /**
     * 构建请求地址
     * @param url String
     * @return String
     */
    private static URI buildRequestUrl(@NonNull String url){
        return URI.create(DEEPSEEK_BASE_URL + url);
    }
    /**
     * 构建请求头
     * @param uri URI
     * @return Builder
     */
    private HttpRequest.Builder generateRequestBuilder(URI uri) {
        return HttpRequest.newBuilder(uri)
                .header("Content-Type", "application/json")
                .header("Authorization", DEEPSEEK_API_KEY)
                .timeout(Duration.ofSeconds(DeepSeek_Timeout));
    }

    @Override
    public void completions(StudyJavaDeepSeekCompletionsVo studyJavaDeepSeekCompletionsVo, SseEmitter emitter) {
        executorService.submit(() -> {
            try {
                HttpRequest httpRequest = generateRequestBuilder(buildRequestUrl(DEEPSEEK_COMPLETIONS_URL))
                        .POST(studyJavaDeepSeekCompletionsVo.getBodyPublisher())
                        .build();
                HttpResponse<InputStream> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofInputStream());
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.body(), StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        try {
                            log.info("line {}",line);
                            emitter.send(line);
                        } catch (IOException e) {
                            emitter.completeWithError(e);
                            break;
                        }
                    }
                } catch (IOException e) {
                    log.error("Error reading response stream {}", e.getMessage());
                    emitter.completeWithError(e);
                } finally {
                    emitter.complete();
                }
            } catch (IOException | InterruptedException e) {
                log.error("Error during HTTP httpRequest {}", e.getMessage());
                emitter.completeWithError(e);
            }
        });
    }

    /**
     * 获取当前api-key所对应的模型
     * @return StudyJavaDeepSeekModelsDto
     */
    @Override
    public StudyJavaDeepSeekModelsDto models() throws IOException, InterruptedException {
        HttpRequest httpRequest = generateRequestBuilder(buildRequestUrl(DEEPSEEK_MODELS_URL))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        log.info("状态码 {}",response.statusCode());
        log.info("响应体 {}",response.body());
        if(response.statusCode() == 200) {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response.body(), StudyJavaDeepSeekModelsDto.class);
        } else {
            throw new StudyJavaException("获取模型失败");
        }
    }
}

