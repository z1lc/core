package com.robertsanek.util;

import org.junit.jupiter.api.Test;

public class LogTest {

  private static final Log log = Logs.getLog(LogTest.class);

  @Test
  public void exceptionUtils() {
    log.error(new RuntimeException("hi"));
  }
}