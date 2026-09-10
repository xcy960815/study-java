package com.studyjava.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

class JsonUtilsTest {

  private record SamplePayload(String name, int count, List<String> tags) {}

  @Test
  void toJsonSerializesObjectToJsonString() {
    Map<String, Object> data = new LinkedHashMap<>();
    data.put("name", "study-java");
    data.put("count", 3);

    String json = JsonUtils.toJson(data);

    assertEquals("{\"name\":\"study-java\",\"count\":3}", json);
  }

  @Test
  void fromJsonDeserializesJsonStringToObject() {
    SamplePayload payload =
        JsonUtils.fromJson(
            "{\"name\":\"java\",\"count\":2,\"tags\":[\"a\"]}", SamplePayload.class);

    assertEquals("java", payload.name());
    assertEquals(2, payload.count());
    assertEquals(List.of("a"), payload.tags());
  }

  @Test
  void roundTripPreservesData() {
    SamplePayload original = new SamplePayload("hello", 5, List.of("x", "y"));

    SamplePayload restored = JsonUtils.fromJson(JsonUtils.toJson(original), SamplePayload.class);

    assertEquals(original, restored);
  }

  @Test
  void fromJsonReturnsNullForNullLiteral() {
    assertNull(JsonUtils.fromJson("null", SamplePayload.class));
  }

  @Test
  void fromJsonThrowsRuntimeExceptionOnMalformedJson() {
    RuntimeException exception =
        assertThrows(
            RuntimeException.class, () -> JsonUtils.fromJson("{not json", SamplePayload.class));

    assertEquals("JSON 解析失败", exception.getMessage());
  }

  @Test
  void toJsonThrowsRuntimeExceptionOnUnknownPropertyMappingFailure() {
    // 用含无法序列化值的 Map 触发 JsonProcessingException
    Map<String, Object> data = new LinkedHashMap<>();
    data.put(
        "value",
        new Object() {
          @Override
          public String toString() {
            throw new IllegalStateException("boom");
          }
        });

    RuntimeException exception =
        assertThrows(RuntimeException.class, () -> JsonUtils.toJson(data));

    assertEquals("JSON 转换失败", exception.getMessage());
  }
}
