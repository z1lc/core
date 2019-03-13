package com.robertsanek.data.etl.remote.humanapi;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.robertsanek.data.etl.remote.humanapi.entities.GenericReading;

public class WeightEtlTest {

  @Test
  @Ignore("integration")
  public void name() {
    List<GenericReading> objects = new WeightEtl().getObjects();
    System.out.println("objects = " + objects);
  }
}