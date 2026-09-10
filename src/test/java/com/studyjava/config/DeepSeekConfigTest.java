package com.studyjava.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/** DeepSeek 配置默认值测试，不发起真实 HTTP 调用。 */
class DeepSeekConfigTest {

  @Test
  void defaultBaseUrlIsDeepSeekApi() {
    DeepSeekConfig config = new DeepSeekConfig();

    assertEquals("https://api.deepseek.com", config.getBaseUrl());
    assertNull(config.getKey());
  }
}
