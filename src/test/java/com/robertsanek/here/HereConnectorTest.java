package com.robertsanek.here;

import java.time.LocalTime;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class HereConnectorTest {

  @Test
  @Disabled("integration")
  public void integration() {
    LocalTime todaysSundownTimeForSanFrancisco = new HereConnector().getTodaysSundownTimeForSanFrancisco();
    System.out.println("todaysSundownTimeForSanFrancisco = " + todaysSundownTimeForSanFrancisco);
  }

}