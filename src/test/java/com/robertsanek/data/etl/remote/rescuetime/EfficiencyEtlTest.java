package com.robertsanek.data.etl.remote.rescuetime;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.robertsanek.util.inject.InjectUtils;

public class EfficiencyEtlTest {

  @Test
  @Disabled("integration")
  public void name() throws Exception {
    List<Efficiency> objects = InjectUtils.inject(EfficiencyEtl.class).getObjects();
    System.out.println("objects = " + objects);
  }
}