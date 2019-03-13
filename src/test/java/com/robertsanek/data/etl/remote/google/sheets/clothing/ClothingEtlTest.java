package com.robertsanek.data.etl.remote.google.sheets.clothing;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

public class ClothingEtlTest {

  @Test
  @Ignore("integration")
  public void name() {
    List<ClothingRating> objects = new ClothingEtl().getObjects();
    System.out.println("objects = " + objects);
  }
}