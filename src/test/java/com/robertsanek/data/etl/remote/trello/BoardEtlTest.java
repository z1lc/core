package com.robertsanek.data.etl.remote.trello;

import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

public class BoardEtlTest {

  @Test
  @Ignore("integration")
  public void name() {
    List<TrelloBoard> objects = new BoardEtl().getObjects();
    System.out.println("objects = " + objects);
  }

}