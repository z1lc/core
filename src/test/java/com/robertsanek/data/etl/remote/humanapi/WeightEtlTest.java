package com.robertsanek.data.etl.remote.humanapi;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.robertsanek.data.etl.remote.humanapi.entities.GenericReading;
import com.robertsanek.util.inject.InjectUtils;

public class WeightEtlTest {

  @Test
  @Disabled("integration")
  public void name() {
    List<GenericReading> objects = InjectUtils.inject(WeightEtl.class).getObjects();
    System.out.println("objects = " + objects);
  }
}