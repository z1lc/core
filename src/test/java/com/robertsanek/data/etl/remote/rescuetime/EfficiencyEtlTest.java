package com.robertsanek.data.etl.remote.rescuetime;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

public class EfficiencyEtlTest {

  @Test
  @Ignore("integration")
  public void name() throws Exception {
    List<Efficiency> objects = new EfficiencyEtl().getObjects();
    System.out.println("objects = " + objects);
  }
}