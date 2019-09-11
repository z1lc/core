package com.robertsanek.data.etl.remote.google.sheets.budget;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class RBudgetEtlTest {

  @Test
  @Disabled("integration")
  public void name() throws Exception {
    List<AnnotatedItem> objects = new RBudgetEtl().getObjects();
    System.out.println("objects = " + objects);
  }
}