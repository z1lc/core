package com.robertsanek.data.quality.anki;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.robertsanek.data.derived.anki.HydratedReview;
import com.robertsanek.data.derived.anki.HydratedReviewDeriver;

public class HydratedReviewDeriverTest {

  @Test
  @Disabled("integration")
  public void integration() {
    List<HydratedReview> objects = new HydratedReviewDeriver().getObjects();
    System.out.println("objects = " + objects);
  }
}