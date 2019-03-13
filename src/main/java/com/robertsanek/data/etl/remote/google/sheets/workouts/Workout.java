package com.robertsanek.data.etl.remote.google.sheets.workouts;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "workouts")
public class Workout {

  @Id
  private ZonedDateTime date;
  private BigDecimal value;
  private String comment;

  public ZonedDateTime getDate() {
    return date;
  }

  public BigDecimal getValue() {
    return value;
  }

  public String getComment() {
    return comment;
  }

  public static final class WorkoutBuilder {

    private ZonedDateTime date;
    private BigDecimal value;
    private String comment;

    private WorkoutBuilder() {}

    public static WorkoutBuilder aWorkout() {
      return new WorkoutBuilder();
    }

    public WorkoutBuilder withDate(ZonedDateTime date) {
      this.date = date;
      return this;
    }

    public WorkoutBuilder withValue(BigDecimal value) {
      this.value = value;
      return this;
    }

    public WorkoutBuilder withComment(String comment) {
      this.comment = comment;
      return this;
    }

    public Workout build() {
      Workout workout = new Workout();
      workout.value = this.value;
      workout.comment = this.comment;
      workout.date = this.date;
      return workout;
    }
  }
}
