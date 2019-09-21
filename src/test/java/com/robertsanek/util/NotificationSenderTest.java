package com.robertsanek.util;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class NotificationSenderTest {

  @Test
  @Disabled("integration")
  public void integration() {
    new NotificationSender().sendNotificationDefault("hi", "hello\n\noh hey there");
  }
}