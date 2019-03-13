package com.robertsanek.data.etl.local.sqllite.anki;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

public class ReviewEtlTest {

  @Test
  @Ignore("integration")
  public void name() throws Exception {
    List<Review> objects = new ReviewEtl().getObjects();
    System.out.println("objects = " + objects);
  }
}