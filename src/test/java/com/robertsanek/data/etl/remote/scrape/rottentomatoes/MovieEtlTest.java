package com.robertsanek.data.etl.remote.scrape.rottentomatoes;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

public class MovieEtlTest {

  @Test
  @Ignore("integration")
  public void name() {
    List<Movie> objects = new MovieEtl().getObjects();
    System.out.println("objects = " + objects);
  }
}