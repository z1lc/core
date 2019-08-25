package com.robertsanek.data.etl.local.habitica;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

public class NewHistoryEtlTest {

  @Test
  @Ignore("integration")
  public void name() {
    List<History> objects = new NewHistoryEtl().getObjects();
    System.out.println("objects = " + objects);
  }
}