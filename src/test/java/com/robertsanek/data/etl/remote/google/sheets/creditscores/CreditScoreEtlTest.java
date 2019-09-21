package com.robertsanek.data.etl.remote.google.sheets.creditscores;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.robertsanek.util.inject.InjectUtils;

public class CreditScoreEtlTest {

  @Test
  @Disabled("integration")
  public void integration() throws Exception {
    List<CreditScore> objects = InjectUtils.inject(CreditScoreEtl.class).getObjects();
    System.out.println("objects = " + objects);
  }
}