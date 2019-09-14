package com.robertsanek.data.etl.remote.google.sheets.clothing;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.robertsanek.util.inject.InjectUtils;

public class ClothingEtlTest {

  @Test
  @Disabled("integration")
  public void name() {
    List<ClothingRating> objects = InjectUtils.inject(ClothingEtl.class).getObjects();
    System.out.println("objects = " + objects);
  }
}