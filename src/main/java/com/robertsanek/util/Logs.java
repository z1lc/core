package com.robertsanek.util;

import org.apache.logging.log4j.LogManager;

public class Logs {

  public static Log getLog(Class<?> clazz) {
    return new Log(LogManager.getLogger(clazz));
  }
}
