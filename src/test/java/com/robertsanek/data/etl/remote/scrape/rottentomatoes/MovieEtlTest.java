package com.robertsanek.data.etl.remote.scrape.rottentomatoes;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.robertsanek.util.inject.InjectUtils;

public class MovieEtlTest {

  @Test
  @Disabled("integration")
  public void integration() {
    List<Movie> objects = InjectUtils.inject(MovieEtl.class).getObjects();
    System.out.println("objects = " + objects);
  }
}