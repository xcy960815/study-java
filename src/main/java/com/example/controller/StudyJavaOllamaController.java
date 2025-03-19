package com.example.controller;

import com.example.domain.vo.ollama.StudyJavaOllamaChatVo;
import com.example.domain.vo.ollama.StudyJavaOllamaShowVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.domain.dto.ollama.*;
import com.example.domain.vo.ollama.StudyJavaOllamaDeleteVo;
import com.example.domain.vo.ollama.StudyJavaOllamaGrenerateVo;
import com.example.service.StudyJavaOllamaService;
import com.example.utils.ResponseGenerator;
import com.example.utils.ResponseResult;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.util.ArrayList;
import com.example.domain.vo.ollama.StudyJavaOllamaChatVo.Message;


@Slf4j
@RestController
@RequestMapping("/ollama")
public class StudyJavaOllamaController {
    @Resource
    private StudyJavaOllamaService studyJavaOllamaService;

    /**
     * generate
     * @param studyJavaOllamaGrenerateVo StudyJavaOllamaGrenerateVo
     * @return ResponseResult<StudyJavaOllamaGenerateDto>
     */
    @PostMapping("/generate")
    public ResponseResult<StudyJavaOllamaGenerateDto> generate(@Valid @RequestBody StudyJavaOllamaGrenerateVo studyJavaOllamaGrenerateVo) {
        try {
            return ResponseGenerator.generateSuccessResult(studyJavaOllamaService.generate(studyJavaOllamaGrenerateVo));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * generateStream
     * @param studyJavaOllamaGrenerateVo StudyJavaOllamaGrenerateVo
     * @return ResponseEntity<StreamingResponseBody>
     */
    @PostMapping(value = "/generateStream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter generateStream(@Valid @RequestBody StudyJavaOllamaGrenerateVo studyJavaOllamaGrenerateVo) {
        SseEmitter emitter = new SseEmitter();
        new Thread(() -> studyJavaOllamaService.generateStream(studyJavaOllamaGrenerateVo, emitter)).start();
        return emitter;
    }
    @PostMapping(value = "/completions", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter completions( @Valid @RequestBody StudyJavaOllamaChatVo studyJavaOllamaChatVo) {
        SseEmitter emitter = new SseEmitter();
        new Thread(() -> studyJavaOllamaService.completions(studyJavaOllamaChatVo, emitter)).start();
        return emitter;
    }
    /**
     * 获取标签
     */
    @GetMapping("/tags")
    public ResponseResult<StudyJavaOllamaTagsDto> tags() {
        try {
            return ResponseGenerator.generateSuccessResult(studyJavaOllamaService.tags());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取所有的模型
     */
    @GetMapping("/models")
    public ResponseResult<StudyJavaOllamaModelsDto> models() {
        try {
            return ResponseGenerator.generateSuccessResult(studyJavaOllamaService.models());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取ollama版本
     * @return ResponseResult
     */
    @GetMapping("/version")
    public ResponseResult<StudyJavaOllamaVersionDto> version() {
        try{
            return ResponseGenerator.generateSuccessResult(studyJavaOllamaService.version());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 删除模型
     * @return ResponseResult<Boolean>
     */
    @DeleteMapping("/delete")
    public ResponseResult<Boolean> delete(@RequestBody StudyJavaOllamaDeleteVo studyJavaOllamaDeleteVo) {
        try {
            return ResponseGenerator.generateSuccessResult(studyJavaOllamaService.delete(studyJavaOllamaDeleteVo));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取正在内存中运行的模型
     * @return ResponseResult<StudyJavaOllamaPsDto>
     */
    @GetMapping("/ps")
    public ResponseResult<StudyJavaOllamaPsDto> ps() {
        try {
            return ResponseGenerator.generateSuccessResult(studyJavaOllamaService.ps());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/pull")
    public void pull() {
//        studyJavaOllamaService.pull();
//        return ResponseGenerator.generateSuccessResult(studyJavaOllamaService.ps());
    }

    @PostMapping("/show")
    public ResponseResult<StudyJavaOllamaShowDto> show(StudyJavaOllamaShowVo studyJavaOllamaShowVo) {
       try {
           return ResponseGenerator.generateSuccessResult(studyJavaOllamaService.show(studyJavaOllamaShowVo));
       } catch (IOException | InterruptedException e) {
           throw new RuntimeException(e);
       }
    }
}
