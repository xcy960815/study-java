package com.example.service.imp;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.example.domain.dto.ollama.StudyJavaOllamaGenerateDto;
import com.example.domain.dto.ollama.StudyJavaOllamaTagsDto;
import com.example.domain.dto.ollama.StudyJavaOllamaVersionDto;
import com.example.exception.StudyJavaException;
import com.example.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import com.example.service.StudyJavaOllamaService;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import com.example.domain.dto.ollama.StudyJavaOllamaModelsDto;

//[GIN-debug] POST   /api/pull                 --> github.com/ollama/ollama/server.(*Server).PullHandler-fm (5 handlers)
//[GIN-debug] POST   /api/generate             --> github.com/ollama/ollama/server.(*Server).GenerateHandler-fm (5 handlers)
//[GIN-debug] POST   /api/chat                 --> github.com/ollama/ollama/server.(*Server).ChatHandler-fm (5 handlers)
//[GIN-debug] POST   /api/embed                --> github.com/ollama/ollama/server.(*Server).EmbedHandler-fm (5 handlers)
//[GIN-debug] POST   /api/embeddings           --> github.com/ollama/ollama/server.(*Server).EmbeddingsHandler-fm (5 handlers)
//[GIN-debug] POST   /api/create               --> github.com/ollama/ollama/server.(*Server).CreateHandler-fm (5 handlers)
//[GIN-debug] POST   /api/push                 --> github.com/ollama/ollama/server.(*Server).PushHandler-fm (5 handlers)
//[GIN-debug] POST   /api/copy                 --> github.com/ollama/ollama/server.(*Server).CopyHandler-fm (5 handlers)
//[GIN-debug] DELETE /api/delete               --> github.com/ollama/ollama/server.(*Server).DeleteHandler-fm (5 handlers)
//[GIN-debug] POST   /api/show                 --> github.com/ollama/ollama/server.(*Server).ShowHandler-fm (5 handlers)
//[GIN-debug] POST   /api/blobs/:digest        --> github.com/ollama/ollama/server.(*Server).CreateBlobHandler-fm (5 handlers)
//[GIN-debug] GET    /api/ps                   --> github.com/ollama/ollama/server.(*Server).PsHandler-fm (5 handlers)
//[GIN-debug] POST   /v1/chat/completions      --> github.com/ollama/ollama/server.(*Server).ChatHandler-fm (6 handlers)
//[GIN-debug] POST   /v1/completions           --> github.com/ollama/ollama/server.(*Server).GenerateHandler-fm (6 handlers)
//[GIN-debug] POST   /v1/embeddings            --> github.com/ollama/ollama/server.(*Server).EmbedHandler-fm (6 handlers)
//[GIN-debug] GET    /v1/models                --> github.com/ollama/ollama/server.(*Server).ListHandler-fm (6 handlers)
//[GIN-debug] GET    /v1/models/:model         --> github.com/ollama/ollama/server.(*Server).ShowHandler-fm (6 handlers)
//[GIN-debug] GET    /                         --> github.com/ollama/ollama/server.(*Server).GenerateRoutes.func1 (5 handlers)
//[GIN-debug] GET    /api/tags                 --> github.com/ollama/ollama/server.(*Server).ListHandler-fm (5 handlers)
//[GIN-debug] GET    /api/version              --> github.com/ollama/ollama/server.(*Server).GenerateRoutes.func2 (5 handlers)

@Service
@Slf4j
public class StudyJavaOllamaServiceImp implements StudyJavaOllamaService {
    /**
     * 端口号
     */
    private final Integer Ollama_Port = 11434;

    /**
     * 域名
     */
    private final String Ollama_Domain = "http://localhost";

    /**
     * generate 接口
     */
    private final String Ollama_Generate_Api = "/api/generate";

    /**
     * completions 接口
     */
    private final String Ollama_Completions_Api = "/v1/chat/completions";

    /**
     * tags 接口
     */
    private final String Ollama_Tags_Api = "/api/tags";

    /**
     * models 接口
     */
    private final String Ollama_Models_Api = "/v1/models";

    /**
     * 删除模型接口
     */
    private final String Ollama_Delete_Models_Api = "/api/delete";

    /**
     * 列出运行模型
     */
    private final String Ollama_Ps_Api = "/api/ps";

    /**
     * 获取模型详情
     */
    private final String Ollama_Show_Api = "/v1/show";

    /**
     * 超时时间
     */
    private final int Ollama_Timeout = 60*1000*5;

    /**
     * version 接口
     */
    private final String Ollama_Version_Api = "/api/version";

    public static void main(String[] args){
        StudyJavaOllamaService studyJavaOllamaService = new StudyJavaOllamaServiceImp();
//        studyJavaOllamaService.version();
//        studyJavaOllamaService.generate();
//        studyJavaOllamaService.generateStream();
//        studyJavaOllamaService.models();
//        studyJavaOllamaService.tags();
    }


    private String buildRequestUrl(String url){
        return Ollama_Domain + ":" + Ollama_Port + url;
    }

    /**
     * 非流式 generate 接口
     */
    @Override
    public StudyJavaOllamaGenerateDto generate(){
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "deepseek-r1:14b");
        requestBody.put("prompt", "为什么天空是蓝色的?");
        requestBody.put("stream", false);
        HttpResponse response = HttpRequest
                .post(buildRequestUrl(Ollama_Generate_Api))
                .timeout(Ollama_Timeout)
                .body(JsonUtils.toJson(requestBody))
                .execute();
        if(response.getStatus() == 200) {
            System.out.println(response.body());
            return JsonUtils.fromJson(response.body(), StudyJavaOllamaGenerateDto.class);
        }else {
            throw new StudyJavaException("请求失败");
        }
    }

    /**
     * 流式 generate 接口
     */
    @Override
    public InputStream generateStream() {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "deepseek-r1:14b");
            requestBody.put("prompt", "为什么天空是蓝色的?");
            requestBody.put("stream", true);
            HttpResponse response = HttpRequest
                    .post(buildRequestUrl(Ollama_Generate_Api))
                    .timeout(Ollama_Timeout)
                    .body(JsonUtils.toJson(requestBody))
                    .execute();
        if (response.getStatus() == 200) {
            InputStream inputStream = response.bodyStream();
            System.out.println(inputStream);
            return inputStream;
        } else {
            System.out.println("请求失败，状态码：" + response.getStatus());
            throw new StudyJavaException("请求失败");
        }
    }

    /**
     * 获取所有的模型
     */
    @Override
    public StudyJavaOllamaModelsDto models() {
        HttpResponse response = HttpRequest
                .get(buildRequestUrl(Ollama_Models_Api))
                .header("Content-Type", "application/json")
                .timeout(Ollama_Timeout)
                .execute();
        if(response.getStatus() == 200) {
           return JsonUtils.fromJson(response.body(),StudyJavaOllamaModelsDto.class);
        } else {
            throw new StudyJavaException("请求失败");
        }
    }

    /**
     * 列出本地模型
     */
    @Override
    public StudyJavaOllamaTagsDto tags(){
        HttpResponse response = HttpRequest
                .get(buildRequestUrl(Ollama_Tags_Api))
                .header("Content-Type", "application/json")
                .timeout(Ollama_Timeout)
                .execute();
        if(response.getStatus() == 200) {
             return JsonUtils.fromJson(response.body(), StudyJavaOllamaTagsDto.class);
        } else {
            throw new StudyJavaException("请求失败");
        }
    }
    /**
     * 获取ollama的版本
     */
    @Override
    public StudyJavaOllamaVersionDto version() {
        // 发送请求，返回 HttpResponse
        HttpResponse response = HttpRequest.get(buildRequestUrl(Ollama_Version_Api))
                .header("Content-Type", "application/json")
                .timeout(Ollama_Timeout)
                .execute();

        System.out.println(response.getStatus());
        if(response.getStatus() == 200) {
            System.out.println(response.body());
            return JsonUtils.fromJson(response.body(), StudyJavaOllamaVersionDto.class);

        } else {
            throw new StudyJavaException("请求失败");
        }
    }

    /**
     * 删除指定的模型
     * name 模型名称
     */
    @Override
    public void deleteModel(String modelName){
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("name", modelName);
        HttpResponse response = HttpRequest.delete(buildRequestUrl(Ollama_Delete_Models_Api))
                .header("Content-Type", "application/json")
                .timeout(Ollama_Timeout)
                .body(JsonUtils.toJson(requestBody))
                .execute();
        if(response.getStatus() == 200) {
            System.out.println(response.body());
        } else {
            throw new StudyJavaException("请求失败");
        }
    }

    /**
     * 获取加载到内存中的模型
     */
    public void ps() {
        HttpResponse response = HttpRequest.get(buildRequestUrl(Ollama_Ps_Api))
                .header("Content-Type", "application/json")
                .timeout(Ollama_Timeout)
                .execute();
        if (response.getStatus() == 200) {
            System.out.println(response.body());
        } else {
            throw new StudyJavaException("请求失败");
        }
    }

    /**
     * 获取模型详情
     */
    public void show(){
        Map<String,String> requestBody = new HashMap<String,String>();
        requestBody.put("name","deepseek-r1:14b");

        HttpResponse response = HttpRequest.post(buildRequestUrl(Ollama_Show_Api))
                .header("Content-Type", "application/json")
                .body(JsonUtils.toJson(requestBody))
                .timeout(Ollama_Timeout)
                .execute();
        if (response.getStatus() == 200) {
            System.out.println(response.body());
        } else {
            throw new StudyJavaException("请求失败");
        }
    }

}
