package com.robertsanek.process;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class HabiticaLoadAssessmentTest {

  @Test
  @Disabled("integration")
  public void name() {
    new HabiticaLoadAssessment().generateHtmlSummary();
  }

}