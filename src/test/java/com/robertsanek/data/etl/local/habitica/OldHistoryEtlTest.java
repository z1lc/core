package com.robertsanek.data.etl.local.habitica;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

public class OldHistoryEtlTest {

  @Test
  @Ignore("integration")
  public void name() {
    OldHistoryEtl oldHistoryEtl = new OldHistoryEtl();
    List<History> objects = oldHistoryEtl.getObjects();
    System.out.println("objects = " + objects);
  }
}