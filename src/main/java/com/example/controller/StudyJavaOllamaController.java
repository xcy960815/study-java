package com.example.controller;

import com.example.domain.dto.ollama.*;
import com.example.domain.vo.ollama.StudyJavaOllamaDeleteVo;
import com.example.domain.vo.ollama.StudyJavaOllamaGrenerateVo;
import com.example.service.StudyJavaOllamaService;
import com.example.utils.ResponseGenerator;
import com.example.utils.ResponseResult;
import io.jsonwebtoken.io.IOException;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.InputStream;


@RestController
@RequestMapping("/ollama")
public class StudyJavaOllamaController {

    @Resource
    private StudyJavaOllamaService studyJavaOllamaService;

    /**
     * 生成模型
     */
    @PostMapping("/generate")
    public ResponseResult<StudyJavaOllamaGenerateDto> generate(@Valid @RequestBody StudyJavaOllamaGrenerateVo studyJavaOllamaGrenerateVo) {
        return ResponseGenerator.generateSuccessResult(studyJavaOllamaService.generate(studyJavaOllamaGrenerateVo));
    }
    @PostMapping(value ="/generateStream",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<StreamingResponseBody> generateStream(@Valid @RequestBody StudyJavaOllamaGrenerateVo studyJavaOllamaGrenerateVo) {
        StreamingResponseBody streamingResponseBody = out -> {
            try (InputStream inputStream = studyJavaOllamaService.generateStream(studyJavaOllamaGrenerateVo)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                // 逐步读取并写入响应流
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);  // 写入响应流
                    out.flush();  // 强制将数据写入响应流
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        };


        // 设置响应头
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "text/event-stream"); // 使用 text/event-stream
        return new ResponseEntity<>(streamingResponseBody, headers, HttpStatus.OK);
    }
    /**
     * 获取标签
     */
    @GetMapping("/tags")
    public ResponseResult<StudyJavaOllamaTagsDto> tags() {
        return ResponseGenerator.generateSuccessResult(studyJavaOllamaService.tags());
    }

    /**
     * 获取所有的模型
     */
    @GetMapping("/models")
    public ResponseResult<StudyJavaOllamaModelsDto> models() {
       return ResponseGenerator.generateSuccessResult(studyJavaOllamaService.models());
    }

    /**
     * 获取ollama版本
     * @return ResponseResult
     */
    @GetMapping("/version")
    public ResponseResult<StudyJavaOllamaVersionDto> version() {
        return ResponseGenerator.generateSuccessResult(studyJavaOllamaService.version());
    }

    /**
     * 删除模型
     * @return ResponseResult<Boolean>
     */
    @DeleteMapping("/delete")
    public ResponseResult<Boolean> delete(@RequestBody StudyJavaOllamaDeleteVo studyJavaOllamaDeleteVo) {
        return ResponseGenerator.generateSuccessResult(studyJavaOllamaService.delete(studyJavaOllamaDeleteVo));
    }

    /**
    * 获取正在内存中运行的模型
    * @return ResponseResult<StudyJavaOllamaPsDto>
    */
    @GetMapping("/ps")
    public ResponseResult<StudyJavaOllamaPsDto> ps() {
        return ResponseGenerator.generateSuccessResult(studyJavaOllamaService.ps());
    }

    @PostMapping("/pull")
    public void pull() {
        studyJavaOllamaService.pull();
//        return ResponseGenerator.generateSuccessResult(studyJavaOllamaService.ps());
    }

    @PostMapping("/show")
    public ResponseResult<StudyJavaOllamaShowDto> show() {
       return ResponseGenerator.generateSuccessResult(studyJavaOllamaService.show());
    }
}
