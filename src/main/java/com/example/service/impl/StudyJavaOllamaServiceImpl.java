package com.example.service.impl;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import com.example.domain.dto.ollama.*;
import com.example.domain.vo.ollama.*;
import com.example.exception.StudyJavaException;
//import com.example.exception.StudyJavaAiException;
import com.example.service.StudyJavaAiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import com.example.service.StudyJavaOllamaService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.io.*;
import java.net.http.*;
import java.net.http.HttpRequest.Builder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
     * generate 接口
     */
    private static final String Ollama_Generate_Api = "/api/generate";

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

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(30))
            .build();

    private final ExecutorService executorService = Executors.newCachedThreadPool();  // 创建线程池

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
     * 非流式 generate 接口
     * @param studyJavaOllamaGenerateDto StudyJavaOllamaGenerateDto
     * @return StudyJavaOllamaGenerateResponseVo
     */
    @Override
    public StudyJavaOllamaGenerateResponseVo generate(StudyJavaOllamaGenerateDto studyJavaOllamaGenerateDto) throws IOException, InterruptedException {
        // 转换请求DTO为原有的VO对象
        StudyJavaOllamaGrenerateVo studyJavaOllamaGrenerateVo = new StudyJavaOllamaGrenerateVo();
        studyJavaOllamaGrenerateVo.setModel(studyJavaOllamaGenerateDto.getModel());
        studyJavaOllamaGrenerateVo.setPrompt(studyJavaOllamaGenerateDto.getPrompt());
        studyJavaOllamaGrenerateVo.setStream(studyJavaOllamaGenerateDto.getStream());
        
        HttpRequest httpRequest = generateRequestBuilder(generateRequestUrl(Ollama_Generate_Api))
                .POST(studyJavaOllamaGrenerateVo.getBodyPublisher())
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        
        // 将原有DTO转换为新的VO返回
        StudyJavaOllamaGenerateDto dto = handleResponse(response, StudyJavaOllamaGenerateDto.class);
        StudyJavaOllamaGenerateResponseVo responseVo = new StudyJavaOllamaGenerateResponseVo();
        
        // 复制属性
        responseVo.setModel(dto.getModel());
        responseVo.setCreated_at(dto.getCreated_at());
        responseVo.setResponse(dto.getResponse());
        responseVo.setDone(dto.isDone());
        responseVo.setDone_reason(dto.getDone_reason());
        responseVo.setContext(dto.getContext());
        responseVo.setTotal_duration(dto.getTotal_duration());
        responseVo.setLoad_duration(dto.getLoad_duration());
        responseVo.setPrompt_eval_count(dto.getPrompt_eval_count());
        responseVo.setPrompt_eval_duration(dto.getPrompt_eval_duration());
        responseVo.setEval_count(dto.getEval_count());
        responseVo.setEval_duration(dto.getEval_duration());
        
        return responseVo;
    }
    /**
     * 流式 generate 接口
     * @param studyJavaOllamaGrenerateVo StudyJavaOllamaGrenerateVo
     * @param emitter SseEmitter
     */
    @Override
    public void generateStream(StudyJavaOllamaGrenerateVo studyJavaOllamaGrenerateVo, SseEmitter emitter) {
        executorService.execute(() -> {
            HttpRequest httpRequest = generateRequestBuilder(generateRequestUrl(Ollama_Generate_Api))
                    .POST(studyJavaOllamaGrenerateVo.getBodyPublisher())
                    .build();

            try {
                HttpResponse<InputStream> response = httpClient.send(httpRequest, BodyHandlers.ofInputStream());
                int statusCode = response.statusCode();

                if (statusCode != 200) {
                    emitter.completeWithError(new StudyJavaException("请求失败，状态码：" + statusCode));
                    return;
                }

                handleStreamResponse(response, emitter);
            } catch (IOException | InterruptedException e) {
                log.error("HTTP 请求失败", e);
                emitter.completeWithError(e);
                Thread.currentThread().interrupt(); // 保持中断状态
            }
        });
    }

    /**
     * 会话接口
     * @param studyJavaOllamaCompletionsVo StudyJavaOllamaCompletionsVo
     * @param emitter SseEmitter
     */
    @Override
    public void completions(StudyJavaOllamaCompletionsVo studyJavaOllamaCompletionsVo, SseEmitter emitter) {
        try {
            HttpRequest httpRequest = generateRequestBuilder(generateRequestUrl(Ollama_Completions_Api))
                    .POST(studyJavaOllamaCompletionsVo.getBodyPublisher())
                    .build();
            HttpResponse<InputStream> response = httpClient.send(httpRequest, BodyHandlers.ofInputStream());
            handleStreamResponse(response, emitter);
        } catch (IOException | InterruptedException e) {
            log.error("Error during HTTP request: {}", e.getMessage());
            emitter.completeWithError(e);
        }
    }
//    TODO 拉取模型 提示 404 page not found
    public void pull (StudyJavaOllamaPullVo studyJavaOllamaPullVo) {
//        Map<String,String> httpRequestBody = new HashMap<>();
//        httpRequestBody.put("name","deepseek-r1:32b");
//        httpRequestBody.put("insecure","true");
//        httpRequestBody.put("stream","false");
//        HttpResponse response = HttpRequest.post(generateRequestUrl(Ollama_Pull_Api).toString())
//                .header("Content-Type", "application/json")
//                .body(JsonUtils.toJson(httpRequestBody))
//                .timeout(Ollama_Timeout)
//                .execute();
//        System.out.println(response.body());
//        if (response.getStatus() == 200) {
//
////            return JsonUtils.fromJson(response.body(), StudyJavaOllamaPsDto.class);
//        } else {
//            throw new StudyJavaException("请求失败");
//        }
    }

    /**
     * 处理HTTP响应
     */
    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseType) throws IOException {
        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), responseType);
        }
        throw new StudyJavaException(String.format("请求失败，状态码：%d", response.statusCode()));
    }

    /**
     * 处理流式响应
     */
    private void handleStreamResponse(HttpResponse<InputStream> response, SseEmitter emitter) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.body(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                emitter.send(line);
            }
            emitter.complete();
        } catch (IOException e) {
            log.error("处理流式响应失败", e);
            emitter.completeWithError(e);
        }
    }

    /**
     * 关闭资源
     */
    public void shutdown() {
        try {
            executorService.shutdown();
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
