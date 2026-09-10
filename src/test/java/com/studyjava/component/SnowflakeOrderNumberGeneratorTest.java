package com.studyjava.component;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

class SnowflakeOrderNumberGeneratorTest {

  private final SnowflakeOrderNumberGenerator generator = new SnowflakeOrderNumberGenerator();

  @Test
  void generatesNumericOrderNumberWithinTwentyDigits() {
    String orderNumber = generator.nextOrderNumber();

    assertTrue(orderNumber.matches("\\d+"), "订单号应为纯数字: " + orderNumber);
    assertTrue(orderNumber.length() <= 20, "订单号不应超过 20 位: " + orderNumber);
  }

  @Test
  void consecutiveCallsGenerateDistinctNumbers() {
    String first = generator.nextOrderNumber();
    String second = generator.nextOrderNumber();

    assertNotEquals(first, second);
  }

  @Test
  void batchGenerationHasNoDuplicates() {
    Set<String> numbers = new HashSet<>();

    for (int i = 0; i < 1000; i++) {
      numbers.add(generator.nextOrderNumber());
    }

    assertEquals(1000, numbers.size());
  }

  @Test
  void numbersAreMonotonicallyIncreasing() {
    long previous = 0;

    for (int i = 0; i < 100; i++) {
      long current = Long.parseLong(generator.nextOrderNumber());
      assertTrue(current > previous, "雪花 ID 应单调递增: " + current + " vs " + previous);
      previous = current;
    }
  }
}
