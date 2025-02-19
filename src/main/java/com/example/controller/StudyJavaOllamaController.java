package com.example.controller;

import com.example.domain.dto.ollama.*;
import com.example.domain.vo.ollama.StudyJavaOllamaDeleteVo;
import com.example.domain.vo.ollama.StudyJavaOllamaGrenerateVo;
import com.example.service.StudyJavaOllamaService;
import com.example.utils.ResponseGenerator;
import com.example.utils.ResponseResult;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;


@RestController
@RequestMapping("/ollama")
public class StudyJavaOllamaController {

    @Resource
    private StudyJavaOllamaService studyJavaOllamaService;

    /**
     * grenerate
     * @param studyJavaOllamaGrenerateVo
     * @return ResponseResult<StudyJavaOllamaGenerateDto>
     */
    @PostMapping("/generate")
    public ResponseResult<StudyJavaOllamaGenerateDto> generate(@Valid @RequestBody StudyJavaOllamaGrenerateVo studyJavaOllamaGrenerateVo) {
        return ResponseGenerator.generateSuccessResult(studyJavaOllamaService.generate(studyJavaOllamaGrenerateVo));
    }

    /**
     * generateStream
     * @param studyJavaOllamaGrenerateVo StudyJavaOllamaGrenerateVo
     * @return ResponseEntity<StreamingResponseBody>
     */
    @PostMapping(value = "/generateStream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> generateStream(@Valid @RequestBody StudyJavaOllamaGrenerateVo studyJavaOllamaGrenerateVo) {
        return Flux.create(sink -> {
            try (InputStream inputStream = studyJavaOllamaService.generateStream(studyJavaOllamaGrenerateVo)) {
                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                while ((line = reader.readLine()) != null) {
                    if (StringUtils.isBlank(line)) {
                        continue;
                    }

                    sink.next(line); // 推送数据
                    Thread.sleep(100);  // 可选：每次读取后稍作等待，模拟逐步处理
                }
                sink.complete();  // 结束流
            } catch (Exception e) {
                sink.error(e);  // 出现异常时推送错误
            }
        });
    }

    @GetMapping(value = "/fluxStream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> fluxStream() {
        return Flux.range(1,10)
                .map(i ->
                        ServerSentEvent.builder("ha"+i).id("id"+i).event("event"+i).data("data"+i).build()
                ).delayElements(Duration.ofMillis(500));
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
