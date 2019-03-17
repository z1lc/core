package com.barnacle.lifting;

public enum ExerciseType {
  DEADLIFT("DL"),
  SQUAT("SQ"),
  LUNGE("LUN"),
  BENCH("BEN"),
  ROW("ROW"),
  OVERHEAD_PRESS("OHP");

  private final String columnNameInGoogleSheets;

  ExerciseType(String columnNameInGoogleSheets) {
    this.columnNameInGoogleSheets = columnNameInGoogleSheets;
  }

}
