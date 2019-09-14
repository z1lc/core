package com.robertsanek.data.quality.anki;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.robertsanek.util.inject.InjectUtils;

class FindReplaceAllBadStringsTest {

  @Test
  @Disabled("integration")
  void name() {
    InjectUtils.inject(FindReplaceAllBadStrings.class).runDQ();
  }
}