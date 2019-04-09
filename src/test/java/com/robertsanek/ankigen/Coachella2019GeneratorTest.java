package com.robertsanek.ankigen;

import org.junit.Ignore;
import org.junit.Test;

public class Coachella2019GeneratorTest {

  @Test
  @Ignore("integration")
  public void name() {
    new Coachella2019Generator().writeFiles();
  }
}