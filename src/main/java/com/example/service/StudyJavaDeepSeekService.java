package com.example.service;


import com.example.domain.dto.deepseek.StudyJavaDeepSeekBalanceDto;
import com.example.domain.dto.deepseek.StudyJavaDeepSeekModelsDto;
import com.example.domain.vo.deeseek.StudyJavaDeepSeekCompletionsVo;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

public interface StudyJavaDeepSeekService {
        /**
         * 对话接口
         * @param studyJavaDeepSeekCompletionsVo StudyJavaDeepSeekCompletionsVo
         * @param emitter SseEmitter
         */
        void completions(StudyJavaDeepSeekCompletionsVo studyJavaDeepSeekCompletionsVo, SseEmitter emitter);

        /**
         * 查询当前api-key 可用的模型
         * @return StudyJavaDeepSeekModelsDto
         * @throws IOException
         * @throws InterruptedException
         */
        StudyJavaDeepSeekModelsDto models() throws IOException, InterruptedException;
        /**
         * 查询余额
         * @return StudyJavaDeepSeekBalanceDto
         * @throws IOException
         * @throws InterruptedException
         */
        StudyJavaDeepSeekBalanceDto balance() throws IOException, InterruptedException;
}
