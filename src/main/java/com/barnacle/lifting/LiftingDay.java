package com.barnacle.lifting;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.barnacle.User;

public class LiftingDay {

  private LocalDate day;
  private HeavyOrEndurance heavyOrEndurance;
  private UpperOrLower upperOrLower;
  private Map<User, List<ExerciseDetails>> repsPerBodyPartByUser;

  public LiftingDay(LocalDate day, HeavyOrEndurance heavyOrEndurance, UpperOrLower upperOrLower,
                    Map<User, List<ExerciseDetails>> repsPerBodyPartByUser) {
    this.day = day;
    this.heavyOrEndurance = heavyOrEndurance;
    this.upperOrLower = upperOrLower;
    this.repsPerBodyPartByUser = repsPerBodyPartByUser;
  }

  public LocalDate getDay() {
    return day;
  }

  public HeavyOrEndurance getHeavyOrEndurance() {
    return heavyOrEndurance;
  }

  public UpperOrLower getUpperOrLower() {
    return upperOrLower;
  }

  public Map<User, List<ExerciseDetails>> getRepsPerBodyPartByUser() {
    return repsPerBodyPartByUser;
  }

}
