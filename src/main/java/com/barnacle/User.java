package com.barnacle;

public enum User {
  JANA("jsankova"),
  ROB("z1lc"),
  VIKTOR("vsanek"),
  WILL("will");

  private final String ankiUsername;

  User(String ankiUsername) {
    this.ankiUsername = ankiUsername;
  }

  public String getAnkiUsername() {
    return ankiUsername;
  }
}
