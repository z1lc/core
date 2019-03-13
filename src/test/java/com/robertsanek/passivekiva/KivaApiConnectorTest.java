package com.robertsanek.passivekiva;

import org.junit.Ignore;
import org.junit.Test;
import org.quartz.JobExecutionException;

public class KivaApiConnectorTest {

  @Test
  @Ignore("integration")
  public void name() {
    new KivaApiConnector().exec(null);
  }
}