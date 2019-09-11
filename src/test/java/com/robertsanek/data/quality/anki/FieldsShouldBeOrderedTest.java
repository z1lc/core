package com.robertsanek.data.quality.anki;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class FieldsShouldBeOrderedTest {

  @Test
  @Disabled("integration")
  public void name() {
    new FieldsShouldBeOrdered().runDQ();
  }

}