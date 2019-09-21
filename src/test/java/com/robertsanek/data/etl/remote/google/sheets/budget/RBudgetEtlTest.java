package com.robertsanek.data.etl.remote.google.sheets.budget;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.robertsanek.util.inject.InjectUtils;

public class RBudgetEtlTest {

  @Test
  @Disabled("integration")
  public void integration() throws Exception {
    List<AnnotatedItem> objects = InjectUtils.inject(RBudgetEtl.class).getObjects();
    System.out.println("objects = " + objects);
  }
}