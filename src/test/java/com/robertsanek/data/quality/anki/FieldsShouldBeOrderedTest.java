package com.robertsanek.data.quality.anki;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.robertsanek.util.inject.InjectUtils;

public class FieldsShouldBeOrderedTest {

  @Test
  @Disabled("integration")
  public void integration() {
    InjectUtils.inject(FieldsShouldBeOrdered.class).runDQ();
  }

}