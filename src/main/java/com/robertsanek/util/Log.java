package com.robertsanek.util;

import java.util.function.Consumer;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;

public class Log {

  private final Logger log;

  public Log(Logger log) {
    this.log = log;
  }

  public void info(String template, Object... objects) {
    genericLog(log::info, template, objects);
  }

  public void warn(String template, Object... objects) {
    genericLog(log::warn, template, objects);
  }

  public void error(String template, Object... objects) {
    genericLog(log::error, template, objects);
  }

  public void error(Throwable t) {
    log.error(ExceptionUtils.getStackTrace(t));
  }

  private void genericLog(Consumer<String> consumer, String template, Object... objects) {
    if (objects.length == 0) {
      consumer.accept(template);
      return;
    }
    consumer.accept(String.format(template, objects));
  }

}
