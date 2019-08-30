package com.robertsanek.data.etl.remote.google.sheets.budget;

import java.util.List;

import com.google.inject.Inject;
import com.robertsanek.data.etl.Etl;

public class RBudgetEtl extends Etl<AnnotatedItem> {

  @Inject BudgetGetter budgetGetter;

  @Override
  public List<AnnotatedItem> getObjects() throws Exception {
    return budgetGetter.getData();
  }

}
