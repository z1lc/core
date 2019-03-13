package com.robertsanek.data.quality.anki;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.robertsanek.data.derived.anki.ReviewTimePerTag;
import com.robertsanek.data.derived.anki.ReviewTimePerTagDeriver;

public class ReviewTimePerTagDeriverTest {

  @Test
  @Ignore("integration")
  public void name() {
    List<ReviewTimePerTag> objects = new ReviewTimePerTagDeriver().getObjects();
    System.out.println("objects = " + objects);
  }
}