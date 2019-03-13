package com.robertsanek.wikipedia;

import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;

public class WikipediaConnectorTest {

  @Test
  @Ignore("integration")
  public void integration() {
    new WikipediaConnector().outputTop30PeopleInLastYear();
  }
}