package com.robertsanek.data.etl.local.sqllite.calibre;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

public class CalibreCalibreBookEtlTest {

  @Test
  @Ignore("integration")
  public void name() throws Exception {
    List<CalibreBook> objects = new CalibreBookEtl().getObjects();
    System.out.println("objects = " + objects);
  }
}