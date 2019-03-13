package com.robertsanek.data.etl.local.sqllite.anki;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

public class DeckEtlTest {

  @Test
  @Ignore("integration")
  public void name() throws Exception {
    List<Deck> objects = new DeckEtl().getObjects();
    System.out.println("objects = " + objects);
  }

}