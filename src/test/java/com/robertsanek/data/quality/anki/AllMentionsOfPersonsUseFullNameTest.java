package com.robertsanek.data.quality.anki;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class AllMentionsOfPersonsUseFullNameTest {

  @Test
  @Disabled("integration")
  @SuppressWarnings("deprecation")
  public void integration() {
    new AllMentionsOfPersonsUseFullName().runDQ();
  }
}