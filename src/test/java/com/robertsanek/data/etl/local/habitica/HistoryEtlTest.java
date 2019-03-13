package com.robertsanek.data.etl.local.habitica;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

public class HistoryEtlTest {

  @Test
  @Ignore("integration")
  public void name() {
    HistoryEtl historyEtl = new HistoryEtl();
    List<History> objects = historyEtl.getObjects();
    System.out.println("objects = " + objects);
  }
}