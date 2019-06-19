package com.robertsanek.here;

import java.time.LocalTime;

import org.junit.Ignore;
import org.junit.Test;

public class HereConnectorTest {

  @Test
  @Ignore("integration")
  public void name() {
    LocalTime todaysSundownTimeForSanFrancisco = HereConnector.getTodaysSundownTimeForSanFrancisco();
    System.out.println("todaysSundownTimeForSanFrancisco = " + todaysSundownTimeForSanFrancisco);
  }

}