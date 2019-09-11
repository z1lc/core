package com.robertsanek.data.etl.remote.trello;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class TrelloListEtlTest {

  @Test
  @Disabled("integration")
  public void name() throws Exception {
    List<TrelloList> objects = new TrelloListEtl().getObjects();
    System.out.println("objects = " + objects);
  }
}