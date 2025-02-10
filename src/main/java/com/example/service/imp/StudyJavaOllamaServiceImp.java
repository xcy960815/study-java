package com.example.service.imp;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.exception.StudyJavaException;
import com.example.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import com.example.service.StudyJavaOllamaService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    // 端口号
    private final Integer Ollama_Port = 11434;

    // 域名
    private final String Ollama_Domain = "http://localhost";

    // generate 接口
    private final String Ollama_Generate_Api = "/api/generate";

    // completions 接口
    private final String Ollama_Completions_Api = "/v1/chat/completions";

    // tags 接口
    private final String Ollama_Tags_Api = "/api/tags";

    // models 接口
    private final String Ollama_Models_Api = "/v1/models";

    // 超时时间
    private final int Ollama_Timeout = 60*1000;

    /**
     * version 接口
     */
    private final String Ollama_Version_Api = "/api/version";

    public static void main(String[] args){
        StudyJavaOllamaService studyJavaOllamaService = new StudyJavaOllamaServiceImp();
//        studyJavaOllamaService.version();
//        studyJavaOllamaService.generate();
        studyJavaOllamaService.models();
//        studyJavaOllamaService.tags();
    }

    @Override
    public String buildRequestUrl(String url){
        return Ollama_Domain + ":" + Ollama_Port + url;
    }


    @Override
    public void generate(){
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "deepseek-r1:14b");
        requestBody.put("prompt", "Why is the sky blue?");
        requestBody.put("stream", false);
        String url = Ollama_Domain + ":" + Ollama_Port + Ollama_Generate_Api;
        System.out.println(url);
        HttpResponse response = HttpRequest.post(buildRequestUrl(Ollama_Generate_Api)).timeout(Ollama_Timeout).body(JsonUtils.toJson(requestBody)).execute();
        System.out.println(response.getStatus());
    }

    @Override
    public void generateStream() {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "deepseek-r1:14b");
            requestBody.put("prompt", "Why is the sky blue?");
            requestBody.put("stream", true);
            HttpResponse response = HttpRequest.post(buildRequestUrl(Ollama_Generate_Api)).timeout(Ollama_Timeout).body(JsonUtils.toJson(requestBody)).execute();
            System.out.println(response.getStatus());
    }

    /**
     * 获取所有的模型
     */
    @Override
    public void models() {
        HttpResponse response = HttpRequest.get(buildRequestUrl(Ollama_Models_Api))
                .header("Content-Type", "application/json")
                .timeout(Ollama_Timeout)
                .execute();
        if(response.getStatus() == 200) {
            JSONObject responseBody = JsonUtils.fromJson(response.body(),JSONObject.class);
            System.out.println(responseBody.get("data"));
        } else {
            throw new StudyJavaException("请求失败");
        }
    }

    /**
     * 获取所有的模型
     */
    @Override
    public List<Object> tags(){
        HttpResponse response = HttpRequest.get(Ollama_Domain + ":" + Ollama_Port + Ollama_Tags_Api)
                .header("Content-Type", "application/json")
                .timeout(Ollama_Timeout)
                .execute();
        if(response.getStatus() == 200) {
            JSONObject responseBody = JsonUtils.fromJson(response.body(),JSONObject.class);
            return (List<Object>) responseBody.get("data");

        } else {
            throw new StudyJavaException("请求失败");
        }
    }


    /**
     * 获取ollama的版本
     */
    @Override
    public String version() {
        System.out.println("StudyJavaOllamaServiceImp version" + Ollama_Domain + ":" + Ollama_Port + Ollama_Version_Api);
        // 发送请求，返回 HttpResponse
        HttpResponse response = HttpRequest.get(buildRequestUrl(Ollama_Version_Api))
                .header("Content-Type", "application/json")
                .timeout(Ollama_Timeout)
                .execute();

        System.out.println(response.getStatus());
        if(response.getStatus() == 200) {
            System.out.println(response.body());
          JSONObject responseBody = JsonUtils.fromJson(response.body(),JSONObject.class);
          return (String) responseBody.get("version");

        } else {
            throw new StudyJavaException("请求失败");
        }
    }

    @Override
    public void delete(){

    }



}
