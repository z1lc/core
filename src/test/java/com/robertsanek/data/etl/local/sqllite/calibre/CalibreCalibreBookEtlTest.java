package com.robertsanek.data.etl.local.sqllite.calibre;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.robertsanek.util.inject.InjectUtils;

public class CalibreCalibreBookEtlTest {

  @Test
  @Disabled("integration")
  public void name() throws Exception {
    List<CalibreBook> objects = InjectUtils.inject(CalibreBookEtl.class).getObjects();
    System.out.println("objects = " + objects);
  }
}