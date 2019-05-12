package com.robertsanek.data.etl.remote.trello;

import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

public class CardEtlTest {
  @Test
  @Ignore("integration")
  public void name() {
    List<TrelloCard> objects = new CardEtl().getObjects();
    System.out.println("objects = " + objects);
  }
}