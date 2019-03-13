package com.robertsanek.data.etl.remote.lastfm;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

public class ArtistEtlTest {

  @Test
  @Ignore("integration")
  public void name() {
    List<Artist> objects = new ArtistEtl().getObjects();
    System.out.println("objects = " + objects);
  }

}