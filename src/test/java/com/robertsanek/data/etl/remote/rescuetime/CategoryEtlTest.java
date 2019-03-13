package com.robertsanek.data.etl.remote.rescuetime;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

public class CategoryEtlTest {

  @Test
  @Ignore("integration")
  public void name() throws Exception {
    List<Category> objects = new CategoryEtl().getObjects();
    System.out.println("objects = " + objects);
  }
}