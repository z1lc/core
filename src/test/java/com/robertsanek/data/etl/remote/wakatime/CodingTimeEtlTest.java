package com.robertsanek.data.etl.remote.wakatime;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

public class CodingTimeEtlTest {

  @Test
  @Ignore("integration")
  public void name() throws Exception {
    List<CodingTime> objects = new CodingTimeEtl().getObjects();
    System.out.println("objects = " + objects);
  }
}