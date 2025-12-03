package com.studyjava.controller;

import com.studyjava.domain.vo.ollama.*;
import com.studyjava.domain.dto.ollama.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.studyjava.service.StudyJavaOllamaService;
import com.studyjava.utils.ResponseGenerator;
import com.studyjava.utils.ResponseResult;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;


@Slf4j
@RestController
@RequestMapping("/ollama")
public class StudyJavaOllamaController {
    @Resource
    private StudyJavaOllamaService studyJavaOllamaService;

    @PostMapping(value = "/completions", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter completions( @Valid @RequestBody StudyJavaOllamaCompletionsVo studyJavaOllamaCompletionsVo) {
        // 设置超时时间为5分钟
        SseEmitter emitter = new SseEmitter(300000L);

        // 添加超时处理
        emitter.onTimeout(() -> {
            log.warn("SSE连接超时");
            emitter.complete();
        });

        // 添加完成处理
        emitter.onCompletion(() -> {
            log.info("SSE连接完成");
        });

        // 添加错误处理 - 区分Broken pipe和其他错误
        emitter.onError((ex) -> {
            // 检查是否是Broken pipe错误，这是正常的客户端断开连接
            if (ex instanceof IOException &&
                ex.getCause() instanceof java.io.IOException &&
                ex.getCause().getMessage().contains("Broken pipe")) {
                log.info("客户端正常断开连接");
            } else {
                log.error("SSE连接发生错误", ex);
            }
        });

        try {
            studyJavaOllamaService.completions(studyJavaOllamaCompletionsVo, emitter);
        } catch (Exception e) {
            log.error("启动completions流式处理失败", e);
            emitter.completeWithError(e);
        }

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
            studyJavaOllamaService.delete(studyJavaOllamaDeleteVo);
            return ResponseGenerator.generateSuccessResult();
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
    public void pull(StudyJavaOllamaPullVo studyJavaOllamaPullVo) {
        studyJavaOllamaService.pull(studyJavaOllamaPullVo);
//         ResponseGenerator.generateSuccessResult();
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
