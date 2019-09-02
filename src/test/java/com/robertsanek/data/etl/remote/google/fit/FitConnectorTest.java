package com.robertsanek.data.etl.remote.google.fit;

import org.junit.Ignore;
import org.junit.Test;

import com.robertsanek.data.etl.remote.google.fit.jsonentities.FitResponse;
import com.robertsanek.util.inject.InjectUtils;

public class FitConnectorTest {

  @Test
  @Ignore("integration")
  public void name() {
    FitConnector fitConnector = InjectUtils.inject(FitConnector.class);
    FitResponse bloodPressureReadings = fitConnector.getBloodPressureReadings();
    System.out.println("bloodPressureReadings = " + bloodPressureReadings);
  }
}