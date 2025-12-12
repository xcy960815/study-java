package com.studyjava.service.impl;

import com.studyjava.config.DeepSeekConfig;
import com.studyjava.domain.dto.StudyJavaCompletionsErrorDto;
import com.studyjava.domain.dto.deepseek.StudyJavaDeepSeekBalanceDto;
import com.studyjava.domain.dto.deepseek.StudyJavaDeepSeekModelsDto;
import com.studyjava.domain.vo.deepseek.StudyJavaDeepSeekCompletionsVo;
import com.studyjava.exception.StudyJavaAiException;
import com.studyjava.exception.StudyJavaException;
import com.studyjava.service.StudyJavaAiService;
import com.studyjava.service.StudyJavaDeepSeekService;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
public class StudyJavaDeepSeekServiceImpl extends StudyJavaAiService implements StudyJavaDeepSeekService {
    @Setter
    @Getter
    @Autowired
    private  DeepSeekConfig deepSeekConfig;

    /**
     * completions 接口地址
     */
    private static final String DEEPSEEK_COMPLETIONS_URL = "/v1/chat/completions";
    /**
     * models
     */
    private static final String DEEPSEEK_MODELS_URL = "/v1/models";
    /**
     * 查询余额接口
     */
    private static final String DEEPSEEK_BALANCE_URL = "/user/balance";

    /**
     * 超时时间 10 分钟
     */
    private static final int DeepSeek_Timeout = 60 * 1000 * 10;

    /**
     * 构建请求地址
     * @param url String
     * @return String
     */
    private URI generateRequestUrl(@NonNull String url){
        return URI.create(deepSeekConfig.getBaseUrl() + url);
    }
    /**
     * 构建请求头
     * @param uri URI
     * @return Builder
     */
    private HttpRequest.Builder generateRequestBuilder(URI uri) {
        String Authorization = String.format("Bearer %s", deepSeekConfig.getKey());
        return HttpRequest.newBuilder(uri)
                .header("Content-Type", "application/json")
                .header("Authorization", Authorization)
                .timeout(Duration.ofMillis(DeepSeek_Timeout));
    }

    @Override
    public void completions(StudyJavaDeepSeekCompletionsVo studyJavaDeepSeekCompletionsVo, SseEmitter emitter) {
        CompletableFuture.runAsync(() -> {
            log.info("开始处理DeepSeek AI对话请求，请求参数：model={}, stream={}, messages.size={}",
                    studyJavaDeepSeekCompletionsVo.getModel(),
                    studyJavaDeepSeekCompletionsVo.getStream(),
                    studyJavaDeepSeekCompletionsVo.getMessages() != null ? studyJavaDeepSeekCompletionsVo.getMessages().size() : 0);

            try {
                // 构建请求URL
                URI requestUrl = generateRequestUrl(DEEPSEEK_COMPLETIONS_URL);
                log.debug("构建请求URL：{}", requestUrl);

                // 构建请求体
                HttpRequest.BodyPublisher bodyPublisher = studyJavaDeepSeekCompletionsVo.getBodyPublisher();
                log.debug("构建请求体完成");

                // 构建HTTP请求
                HttpRequest httpRequest = generateRequestBuilder(requestUrl)
                        .POST(bodyPublisher)
                        .build();
                log.debug("构建HTTP请求完成");

                // 发送请求
                log.info("开始发送HTTP请求到DeepSeek AI");
                HttpResponse<InputStream> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofInputStream());
                log.info("收到DeepSeek AI响应，状态码：{}", response.statusCode());

                // 处理响应
                log.debug("开始处理响应数据流");
                readResponseLines(response, emitter);
                log.info("响应数据处理完成");
                emitter.complete();

            } catch (IOException e) {
                log.error("DeepSeek AI对话请求IO异常：{}", e.getMessage(), e);
                emitter.completeWithError(e);
            } catch (InterruptedException e) {
                log.error("DeepSeek AI对话请求被中断：{}", e.getMessage(), e);
                Thread.currentThread().interrupt(); // 保持中断状态
                emitter.completeWithError(e);
            } catch (Exception e) {
                log.error("DeepSeek AI对话请求发生未知异常：{}", e.getMessage(), e);
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
        HttpRequest httpRequest = generateRequestBuilder(generateRequestUrl(DEEPSEEK_MODELS_URL))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return handleResponse(response, StudyJavaDeepSeekModelsDto.class);
    }

    /**
     * 查询余额
     * @return StudyJavaDeepSeekBalanceDto
     */
    public StudyJavaDeepSeekBalanceDto balance() throws IOException, InterruptedException {
        HttpRequest httpRequest = generateRequestBuilder(generateRequestUrl(DEEPSEEK_BALANCE_URL))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return handleResponse(response, StudyJavaDeepSeekBalanceDto.class);
    }
}

