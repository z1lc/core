package com.robertsanek.ankigen;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class ArtistGeneratorTest {

  @Test
  @Disabled("integration")
  public void integration() {
    new ArtistGenerator().writeFiles();
    System.out.println("persons");
  }
}