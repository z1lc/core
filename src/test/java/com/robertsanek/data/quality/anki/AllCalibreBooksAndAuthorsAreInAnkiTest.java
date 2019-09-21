package com.robertsanek.data.quality.anki;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class AllCalibreBooksAndAuthorsAreInAnkiTest {

  @Test
  @Disabled("integration")
  public void integration() {
    new AllCalibreBooksAndAuthorsAreInAnki().runDQ();
  }
}