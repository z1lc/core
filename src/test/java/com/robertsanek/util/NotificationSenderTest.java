package com.robertsanek.util;

import org.junit.Ignore;
import org.junit.Test;

public class NotificationSenderTest {

  @Test
  @Ignore("integration")
  public void name() {
    new NotificationSender().sendNotificationDefault("hi", "hello\n\noh hey there");
  }
}