package com.robertsanek.data.etl.local.sqllite.anki;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class DeckEtlTest {

  @Test
  @Disabled("integration")
  public void name() throws Exception {
    List<Deck> objects = new DeckEtl().getObjects();
    System.out.println("objects = " + objects);
  }

}