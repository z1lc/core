package com.robertsanek.data.etl.remote.google.sheets.workouts;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

public class WorkoutEtlTest {

  @Test
  @Ignore("integration")
  public void name() {
    List<Workout> objects = new WorkoutEtl().getObjects();
    System.out.println("objects = " + objects);
  }
}