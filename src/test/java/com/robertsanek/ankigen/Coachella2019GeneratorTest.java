package com.robertsanek.ankigen;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class Coachella2019GeneratorTest {

  @Test
  @Disabled("integration")
  public void name() {
    new Coachella2019Generator().writeFiles();
  }
}