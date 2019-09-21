package com.robertsanek.data.quality.anki;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class RenameAllPersonImagesToUniformNamingTest {

  @Test
  @Disabled("integration")
  public void integration() {
    new RenameAllPersonImagesToUniformNaming().runDQ();
  }

}