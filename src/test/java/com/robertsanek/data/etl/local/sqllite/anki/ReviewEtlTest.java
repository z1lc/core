package com.robertsanek.data.etl.local.sqllite.anki;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class ReviewEtlTest {

  @Test
  @Disabled("integration")
  public void name() throws Exception {
    List<Review> objects = new ReviewEtl().getObjects();
    System.out.println("objects = " + objects);
  }
}