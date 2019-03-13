package com.robertsanek.here;

import org.junit.Ignore;
import org.junit.Test;

public class HereConnectorTest {

  @Test
  @Ignore("integration")
  public void name() {
    HereConnector.getTodaysSundownTimeForSanFrancisco();
  }

}