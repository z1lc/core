package com.robertsanek.data.etl.remote.google.sheets.budget;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

public class RBudgetEtlTest {

  @Test
  @Ignore("integration")
  public void name() throws Exception {
    List<AnnotatedItem> objects = new RBudgetEtl().getObjects();
    System.out.println("objects = " + objects);
  }
}