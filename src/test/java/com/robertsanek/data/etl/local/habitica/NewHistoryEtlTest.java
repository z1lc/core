package com.robertsanek.data.etl.local.habitica;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class NewHistoryEtlTest {

  @Test
  @Disabled("integration")
  public void name() {
    List<History> objects = new NewHistoryEtl().getObjects();
    System.out.println("objects = " + objects);
  }
}