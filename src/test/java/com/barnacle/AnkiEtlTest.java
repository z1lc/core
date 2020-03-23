package com.barnacle;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.robertsanek.util.inject.InjectUtils;

public class AnkiEtlTest {

  @Test
  @Disabled("integration")
  void name() {
    InjectUtils.inject(AnkiEtl.class).call();
  }
}