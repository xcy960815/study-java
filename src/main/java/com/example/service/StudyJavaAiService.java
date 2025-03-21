package com.example.service;

import com.example.domain.dto.StudyJavaAiErrorDto;
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

    private static final ObjectMapper objectMapper = new ObjectMapper();

    protected void readResponseLines(HttpResponse<InputStream> response, SseEmitter emitter) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.body(), StandardCharsets.UTF_8))) {
            int statusCode = response.statusCode();
            String line;
            while ((line = reader.readLine()) != null) {
                log.info("line: {}", line);
                if (statusCode == 200) {
                    emitter.send(line);
                }else {
                    StudyJavaAiErrorDto studyJavaAiErrorDto = objectMapper.readValue(response.body(), StudyJavaAiErrorDto.class);
                    throw new StudyJavaAiException(studyJavaAiErrorDto.getError().getCode(), studyJavaAiErrorDto.getError().getMessage());
                }
            }
        }
    }
}
