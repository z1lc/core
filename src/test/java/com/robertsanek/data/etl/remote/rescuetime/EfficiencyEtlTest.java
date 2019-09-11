package com.robertsanek.data.etl.remote.rescuetime;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class EfficiencyEtlTest {

  @Test
  @Disabled("integration")
  public void name() throws Exception {
    List<Efficiency> objects = new EfficiencyEtl().getObjects();
    System.out.println("objects = " + objects);
  }
}