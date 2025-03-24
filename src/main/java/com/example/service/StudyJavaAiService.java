package com.example.service;

import com.example.domain.dto.StudyJavaCompletionsErrorDto;
//import com.example.domain.dto.StudyJavaCompletionsDto;
import com.example.exception.StudyJavaAiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Slf4j
public abstract class StudyJavaAiService {
    /**
     * 数据前缀
     */
    private static final String DATA_PREFIX = "data: ";

    /**
     * keep-alive
     */
    private static final String KEEPALIVE_FLAG = " : keep-alive";
    /**
     * object mapper
     */
    private static final ObjectMapper objectMapper = new ObjectMapper();

    protected void readResponseLines(HttpResponse<InputStream> response, SseEmitter emitter) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.body(), StandardCharsets.UTF_8))) {
            int statusCode = response.statusCode();
            String line;
            while ((line = reader.readLine()) != null ) {
                if (statusCode == 200) {
                    if(!line.trim().isEmpty() && line.startsWith(DATA_PREFIX) && !line.startsWith(KEEPALIVE_FLAG)) {
//                        StudyJavaCompletionsDto studyJavaCompletionsDto = objectMapper.readValue(line.substring(6), StudyJavaCompletionsDto.class);
//                        emitter.send(studyJavaCompletionsDto);
                        String responseLine = line.substring(DATA_PREFIX.length());
                        emitter.send(responseLine);
                    }
                }else {
                    StudyJavaCompletionsErrorDto studyJavaCompletionsErrorDto = objectMapper.readValue(response.body(), StudyJavaCompletionsErrorDto.class);
                    throw new StudyJavaAiException(studyJavaCompletionsErrorDto.getError().getCode(), studyJavaCompletionsErrorDto.getError().getMessage());
                }
            }
        }
    }
}
