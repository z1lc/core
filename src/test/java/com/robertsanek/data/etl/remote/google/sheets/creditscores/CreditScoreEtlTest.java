package com.robertsanek.data.etl.remote.google.sheets.creditscores;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

public class CreditScoreEtlTest {

  @Test
  @Ignore("integration")
  public void name() throws Exception {
    List<CreditScore> objects = new CreditScoreEtl().getObjects();
    System.out.println("objects = " + objects);
  }
}