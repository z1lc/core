package com.robertsanek.ankigen;

import org.junit.Ignore;
import org.junit.Test;

public class ArtistGeneratorTest {

  @Test
  @Ignore("integration")
  public void name() {
    new ArtistGenerator().writeFiles();
    System.out.println("persons");
  }
}