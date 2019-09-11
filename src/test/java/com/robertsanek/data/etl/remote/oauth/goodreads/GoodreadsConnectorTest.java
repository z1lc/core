package com.robertsanek.data.etl.remote.oauth.goodreads;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class GoodreadsConnectorTest {

  @Test
  @Disabled("integration")
  public void name() throws Exception {
    new GoodreadsConnector().getTasks();
  }
}