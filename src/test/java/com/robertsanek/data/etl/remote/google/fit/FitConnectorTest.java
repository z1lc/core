package com.robertsanek.data.etl.remote.google.fit;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.robertsanek.data.etl.remote.google.fit.jsonentities.FitResponse;
import com.robertsanek.util.inject.InjectUtils;

public class FitConnectorTest {

  @Test
  @Disabled("integration")
  public void integration() {
    FitConnector fitConnector = InjectUtils.inject(FitConnector.class);
    FitResponse bloodPressureReadings = fitConnector.getBloodPressureReadings();
    System.out.println("bloodPressureReadings = " + bloodPressureReadings);
  }
}