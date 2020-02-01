package com.robertsanek.here;

import java.time.LocalTime;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.robertsanek.util.inject.InjectUtils;

public class HereConnectorTest {

  @Test
  @Disabled("integration")
  public void integration() {
    LocalTime todaysSundownTimeForSanFrancisco = InjectUtils.inject(HereConnector.class).getTodaysSundownTimeForSanFrancisco();
    System.out.println("todaysSundownTimeForSanFrancisco = " + todaysSundownTimeForSanFrancisco);
  }

}