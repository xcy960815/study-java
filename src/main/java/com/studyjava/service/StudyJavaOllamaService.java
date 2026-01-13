package com.studyjava.service;

import java.io.IOException;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.studyjava.domain.dto.ollama.*;
import com.studyjava.domain.vo.ollama.*;

public interface StudyJavaOllamaService {
  void completions(StudyJavaOllamaCompletionsVo studyJavaOllamaCompletionsVo, SseEmitter emitter);

  StudyJavaOllamaModelsDto models() throws IOException, InterruptedException;

  StudyJavaOllamaVersionDto version() throws IOException, InterruptedException;

  StudyJavaOllamaTagsDto tags() throws IOException, InterruptedException;

  void delete(StudyJavaOllamaDeleteVo studyJavaOllamaDeleteVo)
      throws IOException, InterruptedException;

  StudyJavaOllamaPsDto ps() throws IOException, InterruptedException;

  StudyJavaOllamaShowDto show(StudyJavaOllamaShowVo studyJavaOllamaShowVo)
      throws IOException, InterruptedException;

  void pull(StudyJavaOllamaPullVo studyJavaOllamaPullVo);
}
