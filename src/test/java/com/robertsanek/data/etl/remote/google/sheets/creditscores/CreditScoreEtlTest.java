package com.robertsanek.data.etl.remote.google.sheets.creditscores;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class CreditScoreEtlTest {

  @Test
  @Disabled("integration")
  public void name() throws Exception {
    List<CreditScore> objects = new CreditScoreEtl().getObjects();
    System.out.println("objects = " + objects);
  }
}