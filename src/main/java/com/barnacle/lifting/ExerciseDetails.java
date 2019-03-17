package com.barnacle.lifting;

import java.util.List;

public class ExerciseDetails {
  private ExerciseType exerciseType;
  private int weight;
  private List<Integer> repsPerSet;

  public ExerciseDetails(ExerciseType exerciseType, int weight, List<Integer> repsPerSet) {
    this.exerciseType = exerciseType;
    this.weight = weight;
    this.repsPerSet = repsPerSet;
  }

  public ExerciseType getExerciseType() {
    return exerciseType;
  }

  public int getWeight() {
    return weight;
  }

  public List<Integer> getRepsPerSet() {
    return repsPerSet;
  }
}
