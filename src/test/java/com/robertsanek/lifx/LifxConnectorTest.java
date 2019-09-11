package com.robertsanek.lifx;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class LifxConnectorTest {

  @Test
  @Disabled("integration")
  public void name() {
    boolean b = new LifxConnector().triggerCoreDay();
    System.out.println("b = " + b);
  }
}