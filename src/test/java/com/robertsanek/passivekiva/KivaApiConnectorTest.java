package com.robertsanek.passivekiva;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class KivaApiConnectorTest {

  @Test
  @Disabled("integration")
  public void name() {
    new KivaApiConnector().exec(null);
  }
}