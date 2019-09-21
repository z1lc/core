package com.robertsanek.data.quality.anki;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.robertsanek.data.derived.anki.ReviewTimePerTag;
import com.robertsanek.data.derived.anki.ReviewTimePerTagDeriver;

public class ReviewTimePerTagDeriverTest {

  @Test
  @Disabled("integration")
  public void integration() {
    List<ReviewTimePerTag> objects = new ReviewTimePerTagDeriver().getObjects();
    System.out.println("objects = " + objects);
  }
}