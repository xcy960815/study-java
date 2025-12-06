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

    /**
     * DeepSeek AI 对话接口
     * @param completionsRequestDto 请求参数
     * @return 响应流
     */
    @PostMapping(value = "/completions", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseBodyEmitter completions(@Valid @RequestBody StudyJavaDeepSeekCompletionsRequestDto completionsRequestDto) {
        SseEmitter emitter = new SseEmitter();

        // 转换DTO为VO
        StudyJavaDeepSeekCompletionsVo completionsVo = new StudyJavaDeepSeekCompletionsVo();
        BeanUtils.copyProperties(completionsRequestDto, completionsVo);

        new Thread(() -> studyJavaDeepSeekService.completions(completionsVo, emitter)).start();
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
            throw new StudyJavaException(e.getMessage());
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
            throw new StudyJavaException(e.getMessage());
        }
    }
}
