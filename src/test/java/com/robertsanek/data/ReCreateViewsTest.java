package com.robertsanek.data;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.robertsanek.util.inject.InjectUtils;

public class ReCreateViewsTest {

  @Test
  @Disabled("integration")
  public void integration() {
    InjectUtils.inject(ReCreateViews.class).executeQueries();
  }
}