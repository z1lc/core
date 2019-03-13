package com.robertsanek.data.quality.anki;

import static com.robertsanek.data.quality.anki.RenameAllPersonImagesToUniformNaming.cleanName;
import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

public class RenameAllPersonImagesToUniformNamingTest {

  @Test
  @Ignore("integration")
  public void name() {
    new RenameAllPersonImagesToUniformNaming().runDQ();
  }

}