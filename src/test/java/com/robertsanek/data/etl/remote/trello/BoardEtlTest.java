package com.robertsanek.data.etl.remote.trello;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class BoardEtlTest {

  @Test
  @Disabled("integration")
  public void name() {
    List<TrelloBoard> objects = new BoardEtl().getObjects();
    System.out.println("objects = " + objects);
  }

}