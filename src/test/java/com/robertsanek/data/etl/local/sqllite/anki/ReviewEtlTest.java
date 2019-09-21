package com.robertsanek.data.etl.local.sqllite.anki;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.robertsanek.util.inject.InjectUtils;

public class ReviewEtlTest {

  @Test
  @Disabled("integration")
  public void integration() throws Exception {
    List<Review> objects = InjectUtils.inject(ReviewEtl.class).getObjects();
    System.out.println("objects = " + objects);
  }
}