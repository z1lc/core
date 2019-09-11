package com.barnacle;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class LeetCodeToodledoTaskEtlTest {

  @Test
  @Disabled("integration")
  public void name() {
    new LeetCodeToodledoTaskEtl().run();
  }

}