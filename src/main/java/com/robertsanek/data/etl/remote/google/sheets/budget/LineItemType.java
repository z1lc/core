package com.robertsanek.data.etl.remote.google.sheets.budget;

public enum LineItemType {
  FOOD("FOOD", "Food", true),
  HOUSE("HOUSE", "House", true),
  CLOTHING("CLOTH", "Clothing", true),
  TRANSPORTATION("TRANS", "Transportation", true),
  PERSONAL("PERS", "Personal", true),
  CHARITY("CHAR", "Charity", true),
  HEALTH("HLTH", "Health", true),
  TRAVEL("TRAV", "Travel", true),
  OTHER("OTHR", "Other", true),
  INCOME("Income", "Income", false);

  private final String columnNameInGoogleSheets;
  private final String fullName;
  private final boolean expense;

  LineItemType(String columnNameInGoogleSheets, String fullName, boolean expense) {
    this.columnNameInGoogleSheets = columnNameInGoogleSheets;
    this.fullName = fullName;
    this.expense = expense;
  }

  public String getColumnNameInGoogleSheets() {
    return columnNameInGoogleSheets;
  }

  public String getFullName() {
    return fullName;
  }

  public boolean isExpense() {
    return expense;
  }

}
