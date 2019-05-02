package com.robertsanek.data.quality.anki;

import org.junit.Ignore;
import org.junit.Test;

public class AllMentionsOfPersonsUseFullNameTest {

  @Test
  @Ignore("integration")
  @SuppressWarnings("deprecation")
  public void name() {
    new AllMentionsOfPersonsUseFullName().runDQ();
  }
}