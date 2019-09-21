package com.robertsanek.data.etl.remote.trello;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.robertsanek.util.inject.InjectUtils;

public class TrelloListEtlTest {

  @Test
  @Disabled("integration")
  public void integration() throws Exception {
    List<TrelloList> objects = InjectUtils.inject(TrelloListEtl.class).getObjects();
    System.out.println("objects = " + objects);
  }
}