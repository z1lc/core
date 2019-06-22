package com.robertsanek.data.etl.remote.trello;

import java.util.List;

import org.junit.Test;

public class TrelloListEtlTest {

  @Test
  public void name() throws Exception {
    List<TrelloList> objects = new TrelloListEtl().getObjects();
    System.out.println("objects = " + objects);
  }
}