package com.robertsanek.data.quality.anki;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.robertsanek.util.inject.InjectUtils;

public class AllNotesWithMultipleNamesShouldHaveCorrespondingSynonymCardTest {

  @Test
  @Disabled("integration")
  public void integration() {
    InjectUtils.inject(AllNotesWithMultipleNamesShouldHaveCorrespondingSynonymCard.class).runDQ();
  }
}