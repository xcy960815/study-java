package com.studyjava.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

class IpUtilsTest {

  @Test
  void nullRequestResolvesToUnknown() {
    assertEquals("unknown", IpUtils.getIpAddr(null));
  }

  @Test
  void forwardedForHeaderTakesPrecedence() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader("x-forwarded-for", "203.0.113.10");
    request.setRemoteAddr("10.0.0.1");

    assertEquals("203.0.113.10", IpUtils.getIpAddr(request));
  }

  @Test
  void keepsEntireForwardedForListUnparsed() {
    // 当前实现不拆分 x-forwarded-for 多值，整个字符串原样返回
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader("x-forwarded-for", "203.0.113.10, 198.51.100.7");
    request.setRemoteAddr("10.0.0.1");

    assertEquals("203.0.113.10, 198.51.100.7", IpUtils.getIpAddr(request));
  }

  @Test
  void fallsBackThroughProxyHeadersToRemoteAddr() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader("Proxy-Client-IP", "unknown");
    request.addHeader("WL-Proxy-Client-IP", "");
    request.addHeader("X-Real-IP", "192.0.2.55");
    request.setRemoteAddr("10.0.0.1");

    assertEquals("192.0.2.55", IpUtils.getIpAddr(request));
  }

  @Test
  void usesRemoteAddrWhenAllHeadersMissing() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setRemoteAddr("10.0.0.1");

    assertEquals("10.0.0.1", IpUtils.getIpAddr(request));
  }

  @Test
  void rewritesIpv6LoopbackToIpv4Loopback() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setRemoteAddr("0:0:0:0:0:0:0:1");

    assertEquals("127.0.0.1", IpUtils.getIpAddr(request));
  }

  @Test
  void keepsOtherIpv6AddressesUnchanged() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setRemoteAddr("2001:db8::1");

    assertEquals("2001:db8::1", IpUtils.getIpAddr(request));
  }

  @Test
  void unknownForwardedForValueFallsThrough() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader("x-forwarded-for", "UNKNOWN");
    request.setRemoteAddr("10.0.0.2");

    assertEquals("10.0.0.2", IpUtils.getIpAddr(request));
  }
}
