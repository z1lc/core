package com.barnacle.lifting;

import java.util.List;

import com.google.common.collect.ImmutableList;

public enum UpperOrLower {
  UPPER(ImmutableList.of(ExerciseType.BENCH, ExerciseType.ROW, ExerciseType.OVERHEAD_PRESS)),
  LOWER(ImmutableList.of(ExerciseType.DEADLIFT, ExerciseType.SQUAT, ExerciseType.LUNGE));

  private final ImmutableList<ExerciseType> exerciseTypes;

  UpperOrLower(ImmutableList<ExerciseType> exerciseTypes) {
    this.exerciseTypes = exerciseTypes;
  }

  public List<ExerciseType> getExerciseTypes() {
    return exerciseTypes;
  }

}
