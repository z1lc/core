package com.robertsanek.data.etl.remote.google.sheets.workouts;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.Ignore;
import org.junit.Test;

public class WorkoutEtlTest {

  @Test
  @Ignore("integration")
  public void name() {
    List<Workout> objects = new WorkoutEtl().getObjects();
    System.out.println("objects = " + objects);
  }

  @Test
  public void getTotal() {
    assertEquals(new BigDecimal(0), new WorkoutEtl().getTotal(Optional.empty(), Optional.empty()));
    assertEquals(new BigDecimal(1), new WorkoutEtl().getTotal(Optional.of(new BigDecimal(1)), Optional.empty()));
    assertEquals(new BigDecimal(1), new WorkoutEtl().getTotal(Optional.empty(), Optional.of(new BigDecimal(1))));
    assertEquals(new BigDecimal(2),
        new WorkoutEtl().getTotal(Optional.of(new BigDecimal(1)), Optional.of(new BigDecimal(1))));
  }
}