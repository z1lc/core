package com.robertsanek.data.etl.remote.wakatime;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class CodingTimeEtlTest {

  @Test
  @Disabled("integration")
  public void name() throws Exception {
    List<CodingTime> objects = new CodingTimeEtl().getObjects();
    System.out.println("objects = " + objects);
  }
}