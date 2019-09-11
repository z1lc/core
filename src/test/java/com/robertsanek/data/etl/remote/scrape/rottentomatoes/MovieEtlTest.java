package com.robertsanek.data.etl.remote.scrape.rottentomatoes;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class MovieEtlTest {

  @Test
  @Disabled("integration")
  public void name() {
    List<Movie> objects = new MovieEtl().getObjects();
    System.out.println("objects = " + objects);
  }
}