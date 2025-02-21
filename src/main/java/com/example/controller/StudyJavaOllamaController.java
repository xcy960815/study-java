package com.example.controller;

import com.example.domain.vo.ollama.StudyJavaOllamaShowVo;
import com.example.exception.StudyJavaException;
import com.example.service.DataCallback;
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

        SseEmitter emitter = new SseEmitter();  // 创建一个 SseEmitter 实例
        // 创建回调实现
        DataCallback dataCallback = new DataCallback() {
            @Override
            public void onDataReceived(String data) {
                try {
                    log.info("onDataReceived: {}", data);
                    emitter.send(data);  // 将数据发送到前端
                } catch (IOException e) {
                    emitter.completeWithError(e);  // 发生错误时通知前端
                }
            }

            @Override
            public void onComplete() {
                emitter.complete();  // 完成数据发送
            }

            @Override
            public void onError(Exception e) {
                emitter.completeWithError(e);  // 错误时通知前端
            }
        };
        try {
            // 调用 service 层逐步返回数据
            studyJavaOllamaService.generateStream(studyJavaOllamaGrenerateVo, dataCallback);
        } catch (IOException | InterruptedException e) {
            emitter.completeWithError(e);  // 如果出错，通知前端
        }
        return emitter;  // 返回 SseEmitter 给客户端
    }

    @GetMapping(value = "/fluxStream")
    public SseEmitter fluxStream() {
        // 用于创建一个 SSE 连接对象
        SseEmitter emitter = new SseEmitter();
        try {
            // 在后台线程中模拟实时数据
            new Thread(() -> {
                log.info("=======================streamTest new Thread start");
                try {
                    for (int i = 0; i < 10; i++) {
                        // emitter.send() 方法向客户端发送消息
                        // 使用SseEmitter.event()创建一个事件对象，设置事件名称和数据
                        emitter.send(SseEmitter.event().name("message").data("===========>[" + i + "] Data #" + i));
                        log.info("=======================streamTest push data==>" + i);
                        Thread.sleep(1000);
                    }
                    // 数据发送完成后，关闭连接
                    emitter.complete();
                } catch (IOException | InterruptedException e) {
                    // 发生错误时，关闭连接并报错
                    emitter.completeWithError(e);
                    log.error("=======================streamTest push data error==>" + e);
                }
            }).start();
            log.info("=======================streamTest end");
        } catch (Exception e) {
            log.error("streamTest error!", e);
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

//    /**
//     * 删除模型
//     * @return ResponseResult<Boolean>
//     */
//    @DeleteMapping("/delete")
//    public ResponseResult<Boolean> delete(@RequestBody StudyJavaOllamaDeleteVo studyJavaOllamaDeleteVo) {
//        return ResponseGenerator.generateSuccessResult(studyJavaOllamaService.delete(studyJavaOllamaDeleteVo));
//    }

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
