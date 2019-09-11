package com.robertsanek.data;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class ReCreateViewsTest {

  @Test
  @Disabled("integration")
  public void name() {
    new ReCreateViews().executeQueries();
  }
}