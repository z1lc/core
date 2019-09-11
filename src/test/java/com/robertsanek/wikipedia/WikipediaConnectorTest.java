package com.robertsanek.wikipedia;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class WikipediaConnectorTest {

  @Test
  @Disabled("integration")
  public void integration() {
    new WikipediaConnector().outputTop30PeopleInLastYear();
  }
}