package com.example.service.imp;

import com.example.config.DeepSeekConfig;
import com.example.domain.dto.StudyJavaCompletionsErrorDto;
import com.example.domain.dto.deepseek.StudyJavaDeepSeekBalanceDto;
import com.example.domain.dto.deepseek.StudyJavaDeepSeekModelsDto;
import com.example.domain.vo.deeseek.StudyJavaDeepSeekCompletionsVo;
import com.example.exception.StudyJavaAiException;
import com.example.exception.StudyJavaException;
import com.example.service.StudyJavaAiService;
import com.example.service.StudyJavaDeepSeekService;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
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
public class StudyJavaDeepSeekServiceImp extends StudyJavaAiService implements StudyJavaDeepSeekService {
    @Setter
    @Getter
    @Autowired
    private  DeepSeekConfig deepSeekConfig;
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
     * 查询余额接口
     */
    private static final String DEEPSEEK_BALANCE_URL = "/user/balance";

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
//    private final ExecutorService executorService = Executors.newCachedThreadPool();

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 构建请求地址
     * @param url String
     * @return String
     */
    private static URI generateRequestUrl(@NonNull String url){
        return URI.create(DEEPSEEK_BASE_URL + url);
    }
    /**
     * 构建请求头
     * @param uri URI
     * @return Builder
     */
    private HttpRequest.Builder generateRequestBuilder(URI uri) {
        String Authorization = String.format("Bearer %s", deepSeekConfig.getApiKey());
        return HttpRequest.newBuilder(uri)
                .header("Content-Type", "application/json")
                .header("Authorization", Authorization)
                .timeout(Duration.ofSeconds(DeepSeek_Timeout));
    }

    @Override
    public void completions(StudyJavaDeepSeekCompletionsVo studyJavaDeepSeekCompletionsVo, SseEmitter emitter) {
        try {
            HttpRequest httpRequest = generateRequestBuilder(generateRequestUrl(DEEPSEEK_COMPLETIONS_URL))
                    .POST(studyJavaDeepSeekCompletionsVo.getBodyPublisher())
                    .build();
            HttpResponse<InputStream> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofInputStream());
            readResponseLines(response,emitter);
        } catch (IOException | InterruptedException e) {
            log.error("Error during HTTP request: {}", e.getMessage());
            emitter.completeWithError(e);
        }
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
        if(response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), StudyJavaDeepSeekModelsDto.class);
        } else {
            StudyJavaCompletionsErrorDto studyJavaCompletionsErrorDto = objectMapper.readValue(response.body(), StudyJavaCompletionsErrorDto.class);
//            throw new StudyJavaAiException(studyJavaCompletionsErrorDto.getError().getCode(), studyJavaCompletionsErrorDto.getError().getMessage());
            log.error("获取当前api-key所对应的模型失败，失败原因 {}", studyJavaCompletionsErrorDto.getError().getMessage());
            throw new StudyJavaException(studyJavaCompletionsErrorDto.getError().getMessage());
        }
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
        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), StudyJavaDeepSeekBalanceDto.class);
        } else {
            StudyJavaCompletionsErrorDto studyJavaCompletionsErrorDto = objectMapper.readValue(response.body(), StudyJavaCompletionsErrorDto.class);
            throw new StudyJavaAiException(studyJavaCompletionsErrorDto.getError().getCode(), studyJavaCompletionsErrorDto.getError().getMessage());
        }
    }
}

