package com.barnacle;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.robertsanek.util.inject.InjectUtils;

public class LeetCodeToodledoTaskEtlTest {

  @Test
  @Disabled("integration")
  public void name() {
    InjectUtils.inject(LeetCodeToodledoTaskEtl.class).run();
  }

}