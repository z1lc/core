package com.robertsanek.data.quality.anki;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.robertsanek.data.derived.anki.DaysSinceReview;
import com.robertsanek.data.derived.anki.DaysSinceReviewDeriver;

public class DaysSinceReviewDeriverTest {

  @Test
  @Ignore("integration")
  public void name() {
    List<DaysSinceReview> objects = new DaysSinceReviewDeriver().getObjects();
    System.out.println("objects = " + objects);
  }
}