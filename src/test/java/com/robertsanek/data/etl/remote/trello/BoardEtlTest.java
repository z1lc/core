package com.robertsanek.data.etl.remote.trello;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.robertsanek.util.inject.InjectUtils;

public class BoardEtlTest {

  @Test
  @Disabled("integration")
  public void integration() {
    List<TrelloBoard> objects = InjectUtils.inject(BoardEtl.class).getObjects();
    System.out.println("objects = " + objects);
  }

}