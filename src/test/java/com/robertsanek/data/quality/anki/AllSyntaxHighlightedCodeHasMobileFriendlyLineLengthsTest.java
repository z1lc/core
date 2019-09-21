package com.robertsanek.data.quality.anki;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class AllSyntaxHighlightedCodeHasMobileFriendlyLineLengthsTest {

  @Test
  @Disabled("integration")
  public void integration() {
    new AllSyntaxHighlightedCodeHasMobileFriendlyLineLengths().runDQ();
  }
}