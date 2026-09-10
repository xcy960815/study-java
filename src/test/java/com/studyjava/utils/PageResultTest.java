package com.studyjava.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

class PageResultTest {

  @Test
  void ofCarriesDataAndTotal() {
    List<String> data = List.of("a", "b", "c");

    PageResult<String> result = PageResult.of(data, 42);

    assertEquals(data, result.getData());
    assertEquals(42, result.getTotal());
  }

  @Test
  void supportsEmptyPage() {
    PageResult<String> result = PageResult.of(List.of(), 0);

    assertEquals(List.of(), result.getData());
    assertEquals(0, result.getTotal());
  }
}
