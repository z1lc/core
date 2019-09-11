package com.robertsanek.data.etl.local.sqllite.calibre;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class CalibreCalibreBookEtlTest {

  @Test
  @Disabled("integration")
  public void name() throws Exception {
    List<CalibreBook> objects = new CalibreBookEtl().getObjects();
    System.out.println("objects = " + objects);
  }
}