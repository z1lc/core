package com.robertsanek.data.etl.remote.humanapi;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.robertsanek.data.etl.remote.humanapi.entities.GenericReading;

public class WeightEtlTest {

  @Test
  @Disabled("integration")
  public void name() {
    List<GenericReading> objects = new WeightEtl().getObjects();
    System.out.println("objects = " + objects);
  }
}