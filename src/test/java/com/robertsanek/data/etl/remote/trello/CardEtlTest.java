package com.robertsanek.data.etl.remote.trello;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.robertsanek.util.inject.InjectUtils;

public class CardEtlTest {

  @Test
  @Disabled("integration")
  public void name() {
    List<TrelloCard> objects = InjectUtils.inject(CardEtl.class).getObjects();
    System.out.println("objects = " + objects);
  }
}