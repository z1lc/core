package com.robertsanek.data.quality.anki;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.robertsanek.data.derived.anki.DaysSinceReview;
import com.robertsanek.data.derived.anki.DaysSinceReviewDeriver;

public class DaysSinceReviewDeriverTest {

  @Test
  @Disabled("integration")
  public void name() {
    List<DaysSinceReview> objects = new DaysSinceReviewDeriver().getObjects();
    System.out.println("objects = " + objects);
  }
}