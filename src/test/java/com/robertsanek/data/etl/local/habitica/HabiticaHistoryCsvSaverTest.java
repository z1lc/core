package com.robertsanek.data.etl.local.habitica;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

public class HabiticaHistoryCsvSaverTest {

  @Test
  @Ignore("integration")
  public void name() throws IOException {
    new HabiticaHistorySaver().getHistoryAndSaveAsCsv();
  }
}