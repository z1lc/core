package com.robertsanek.data.etl.remote.trello;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class CardEtlTest {

  @Test
  @Disabled("integration")
  public void name() {
    List<TrelloCard> objects = new CardEtl().getObjects();
    System.out.println("objects = " + objects);
  }
}