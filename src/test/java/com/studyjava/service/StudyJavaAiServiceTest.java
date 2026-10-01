package com.studyjava.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.studyjava.domain.dto.deepseek.StudyJavaDeepSeekModelsDto;
import com.studyjava.exception.StudyJavaAiException;

class StudyJavaAiServiceTest {

  /** 暴露受保护方法的测试桩 */
  static class TestableAiService extends StudyJavaAiService {
    <T> T parse(HttpResponse<String> response, Class<T> responseType) throws IOException {
      return handleResponse(response, responseType);
    }

    void read(HttpResponse<InputStream> response, SseEmitter emitter) throws IOException {
      readResponseLines(response, emitter);
    }
  }

  private TestableAiService aiService;
  private SseEmitter emitter;

  @BeforeEach
  void setUp() {
    aiService = new TestableAiService();
    emitter = mock(SseEmitter.class);
  }

  private HttpResponse<String> stringResponse(int statusCode, String body) {
    HttpResponse<String> response = mock(HttpResponse.class);
    when(response.statusCode()).thenReturn(statusCode);
    when(response.body()).thenReturn(body);
    return response;
  }

  private HttpResponse<InputStream> streamResponse(int statusCode, String body) {
    HttpResponse<InputStream> response = mock(HttpResponse.class);
    when(response.statusCode()).thenReturn(statusCode);
    when(response.body())
        .thenReturn(new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8)));
    return response;
  }

  @Test
  void handleResponseParsesSuccessfulBody() throws Exception {
    String body = "{\"object\":\"list\",\"data\":[{\"id\":\"deepseek-chat\","
        + "\"object\":\"model\",\"created\":1700000000,\"owned_by\":\"deepseek\"}]}";

    StudyJavaDeepSeekModelsDto dto =
        aiService.parse(stringResponse(200, body), StudyJavaDeepSeekModelsDto.class);

    assertEquals("list", dto.getObject());
    assertEquals("deepseek-chat", dto.getData().get(0).getId());
  }

  @Test
  void handleResponseThrowsAiExceptionWithApiErrorDetails() {
    String body = "{\"error\":{\"message\":\"Invalid API key\","
        + "\"type\":\"invalid_request_error\",\"code\":\"invalid_api_key\"}}";

    StudyJavaAiException exception =
        assertThrows(
            StudyJavaAiException.class,
            () -> aiService.parse(stringResponse(401, body), StudyJavaDeepSeekModelsDto.class));

    assertEquals("invalid_api_key", exception.getStatusCode());
    assertEquals("Invalid API key", exception.getMessage());
  }

  @Test
  void handleResponseThrowsGenericExceptionWhenErrorBodyUnparsable() {
    RuntimeException exception =
        assertThrows(
            RuntimeException.class,
            () ->
                aiService.parse(
                    stringResponse(500, "<html>boom</html>"),
                    StudyJavaDeepSeekModelsDto.class));

    assertEquals(
        "请求失败，状态码：500，内容：<html>boom</html>", exception.getMessage());
  }

  @Test
  void readResponseLinesForwardsDataEventsToEmitter() throws Exception {
    String body = "data: {\"delta\":\"你\"}\ndata: {\"delta\":\"好\"}\ndata: [DONE]\n";

    aiService.read(streamResponse(200, body), emitter);

    ArgumentCaptor<Object> events = ArgumentCaptor.forClass(Object.class);
    verify(emitter, times(2)).send(events.capture());
    assertEquals("{\"delta\":\"你\"}", events.getAllValues().get(0));
    assertEquals("{\"delta\":\"好\"}", events.getAllValues().get(1));
  }

  @Test
  void readResponseLinesSkipsKeepAliveAndBlankLines() throws Exception {
    String body = " : keep-alive\n\ndata: {\"delta\":\"a\"}\n";

    aiService.read(streamResponse(200, body), emitter);

    ArgumentCaptor<Object> events = ArgumentCaptor.forClass(Object.class);
    verify(emitter).send(events.capture());
    assertEquals("{\"delta\":\"a\"}", events.getValue());
  }

  @Test
  void readResponseLinesThrowsAiExceptionOnNonSuccessStatus() {
    String body =
        "{\"error\":{\"message\":\"Quota exhausted\",\"code\":\"insufficient_balance\"}}";

    StudyJavaAiException exception =
        assertThrows(
            StudyJavaAiException.class,
            () -> aiService.read(streamResponse(429, body), emitter));

    assertEquals("insufficient_balance", exception.getStatusCode());
  }

  @Test
  void readResponseLinesThrowsGenericExceptionWhenErrorStreamUnparsable() {
    RuntimeException exception =
        assertThrows(
            RuntimeException.class,
            () -> aiService.read(streamResponse(502, "bad gateway"), emitter));

    assertTrue(exception.getMessage().contains("502"));
  }

  @Test
  void readResponseLinesHandlesEmptyStreamWithoutError() throws Exception {
    assertDoesNotThrow(() -> aiService.read(streamResponse(200, ""), emitter));
    verify(emitter, never()).send(anyString());
  }
}
