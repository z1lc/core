package com.robertsanek.data.etl.remote.google.sheets.budget;

import java.util.List;

import com.robertsanek.data.etl.Etl;

public class RBudgetEtl extends Etl<AnnotatedItem> {

  @Override
  public List<AnnotatedItem> getObjects() throws Exception {
    return BudgetGetter.getData();
  }

}
