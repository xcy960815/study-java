package com.studyjava.controller;

import com.studyjava.domain.dto.deepseek.StudyJavaDeepSeekBalanceDto;
import com.studyjava.domain.dto.deepseek.StudyJavaDeepSeekCompletionsRequestDto;
import com.studyjava.domain.dto.deepseek.StudyJavaDeepSeekModelsDto;
import com.studyjava.domain.vo.deepseek.StudyJavaDeepSeekBalanceVo;
import com.studyjava.domain.vo.deepseek.StudyJavaDeepSeekCompletionsVo;
import com.studyjava.domain.vo.deepseek.StudyJavaDeepSeekModelsVo;
import com.studyjava.exception.StudyJavaException;
import com.studyjava.service.StudyJavaDeepSeekService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// https://blog.csdn.net/u014390502/article/details/143275309
@RestController
@RequestMapping("/deepseek")
@Slf4j
public class StudyJavaDeepSeekController {

    @Resource
    private StudyJavaDeepSeekService studyJavaDeepSeekService;

    @Resource(name = "aiTaskExecutor")
    private java.util.concurrent.Executor aiTaskExecutor;

    /**
     * DeepSeek AI 对话接口
     * @param completionsRequestDto 请求参数
     * @return 响应流
     */
    @PostMapping(value = "/completions", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseBodyEmitter completions(@Valid @RequestBody StudyJavaDeepSeekCompletionsRequestDto completionsRequestDto) {
        // 设置超时时间，例如 5 分钟
        SseEmitter emitter = new SseEmitter(5 * 60 * 1000L);
        
        emitter.onCompletion(() -> log.debug("SSE连接完成"));
        emitter.onTimeout(() -> {
            log.warn("SSE连接超时");
            emitter.complete();
        });
        emitter.onError((e) -> {
            log.error("SSE连接异常", e);
            emitter.complete();
        });

        // 转换DTO为VO
        StudyJavaDeepSeekCompletionsVo completionsVo = new StudyJavaDeepSeekCompletionsVo();
        BeanUtils.copyProperties(completionsRequestDto, completionsVo);

        // 使用线程池执行异步任务
        aiTaskExecutor.execute(() -> studyJavaDeepSeekService.completions(completionsVo, emitter));
        return emitter;
    }

    /**
     * 获取可用模型列表
     * @return StudyJavaDeepSeekModelsVo
     */
    @GetMapping(value = "/models")
    public StudyJavaDeepSeekModelsVo models() {
        try {
            StudyJavaDeepSeekModelsDto modelsDto = studyJavaDeepSeekService.models();

            // 转换DTO为VO
            StudyJavaDeepSeekModelsVo modelsVo = new StudyJavaDeepSeekModelsVo();
            modelsVo.setObject(modelsDto.getObject());

            List<StudyJavaDeepSeekModelsVo.Model> modelVoList = new ArrayList<>();
            for (StudyJavaDeepSeekModelsDto.Model modelDto : modelsDto.getData()) {
                StudyJavaDeepSeekModelsVo.Model modelVo = new StudyJavaDeepSeekModelsVo.Model();
                BeanUtils.copyProperties(modelDto, modelVo);
                modelVoList.add(modelVo);
            }
            modelsVo.setData(modelVoList);

            return modelsVo;
        } catch (IOException | InterruptedException e) {
            log.error("DeepSeek models error", e);
            throw new StudyJavaException(e.getMessage() != null ? e.getMessage() : "DeepSeek API Error: " + e.getClass().getSimpleName());
        }
    }

    /**
     * 获取账户余额
     * @return StudyJavaDeepSeekBalanceVo
     */
    @GetMapping(value = "/balance")
    public StudyJavaDeepSeekBalanceVo balance() {
        try {
            StudyJavaDeepSeekBalanceDto balanceDto = studyJavaDeepSeekService.balance();

            // 转换DTO为VO
            StudyJavaDeepSeekBalanceVo balanceVo = new StudyJavaDeepSeekBalanceVo();
            balanceVo.setIs_available(balanceDto.getIs_available());

            List<StudyJavaDeepSeekBalanceVo.BalanceInfo> balanceInfoVoList = new ArrayList<>();
            for (StudyJavaDeepSeekBalanceDto.BalanceInfo balanceInfoDto : balanceDto.getBalance_infos()) {
                StudyJavaDeepSeekBalanceVo.BalanceInfo balanceInfoVo = new StudyJavaDeepSeekBalanceVo.BalanceInfo();
                BeanUtils.copyProperties(balanceInfoDto, balanceInfoVo);
                balanceInfoVoList.add(balanceInfoVo);
            }
            balanceVo.setBalance_infos(balanceInfoVoList);

            return balanceVo;
        } catch (IOException | InterruptedException e) {
            log.error("DeepSeek balance error", e);
            throw new StudyJavaException(e.getMessage() != null ? e.getMessage() : "DeepSeek API Error: " + e.getClass().getSimpleName());
        }
    }
}
