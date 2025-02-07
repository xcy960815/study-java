//package com.example.service.imp;
//
//import com.example.service.StudyJavaOllamaService;
//import de.asedem.Ollama;
//import de.asedem.model.GenerationRequest;
//import de.asedem.model.GenerationResponse;
//import de.asedem.model.Model;
//import de.asedem.model.ModelInfo;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@Slf4j
//public class StudyJavaOllamaServiceImp implements StudyJavaOllamaService {
//
//    public static void main(String[] args) {
//        StudyJavaOllamaService studyJavaOllamaService = new StudyJavaOllamaServiceImp();
//        log.info("{}", studyJavaOllamaService.getModels());
//        log.info("{}", studyJavaOllamaService.getModelDetail("deepseek-r1:7b"));
//    }
//
//   private final Ollama ollama = Ollama.init("http://127.0.0.1", 11434);
//
//
//    @Override
//    public List<Model>getModels(){
//        return ollama.listModels();
//    }
//
//    @Override
//    public ModelInfo getModelDetail(String modelName){
//      return ollama.showInfo(modelName);
//    }
//
//    @Override
//    public String generate(){
//        String model = "deepseek-r1:7b"; // 指定模型
//        String prompt = "你好"; // 提供提示
//
//        GenerationResponse response = ollama.generate(new GenerationRequest(model, prompt));
//
//        // 打印生成的响应
//        log.info(response.response());
//        return null;
//    }
//}
package com.example.service.imp;

import com.example.service.StudyJavaOllamaService;
import de.asedem.Ollama;
import de.asedem.model.Model;
import de.asedem.model.ModelInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@Slf4j
public class StudyJavaOllamaServiceImp implements StudyJavaOllamaService {

    private final Ollama ollama = Ollama.init("http://127.0.0.1", 11434);

    public static void main(String[] args) {
        StudyJavaOllamaService studyJavaOllamaService = new StudyJavaOllamaServiceImp();
        log.info("{}", studyJavaOllamaService.getModels());
        log.info("{}", studyJavaOllamaService.getModelDetail("deepseek-r1:7b"));
        studyJavaOllamaService.generate();
    }

    @Override
    public List<Model> getModels() {
        return ollama.listModels();
    }

    @Override
    public ModelInfo getModelDetail(String modelName) {
        return ollama.showInfo(modelName);
    }

    @Override
    public String generate() {
        String model = "deepseek-r1:7b"; // 指定模型
        String prompt = "你好"; // 提供提示

        try {
            // 构造请求的 URL 和数据
            URL url = new URL("http://localhost:11434/api/generate");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // 创建请求体
            String jsonInputString = String.format("{\"model\": \"%s\", \"prompt\": \"%s\"}", model, prompt);

            // 发送请求
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // 读取响应
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                // 打印生成的响应
                log.info(response.toString());
                return response.toString();
            }
        } catch (Exception e) {
            log.error("Error generating response", e);
            return "Error generating response";
        }
    }
}
