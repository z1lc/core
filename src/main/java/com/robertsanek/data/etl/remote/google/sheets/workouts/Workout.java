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
  private BigDecimal cardio;
  private BigDecimal lifting;
  private BigDecimal total;
  private String comment;

  public ZonedDateTime getDate() {
    return date;
  }

  public BigDecimal getCardio() {
    return cardio;
  }

  public BigDecimal getLifting() {
    return lifting;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public String getComment() {
    return comment;
  }

  public static final class WorkoutBuilder {

    private ZonedDateTime date;
    private BigDecimal cardio;
    private BigDecimal lifting;
    private BigDecimal total;
    private String comment;

    private WorkoutBuilder() {}

    public static WorkoutBuilder aWorkout() {
      return new WorkoutBuilder();
    }

    public WorkoutBuilder withDate(ZonedDateTime date) {
      this.date = date;
      return this;
    }

    public WorkoutBuilder withCardio(BigDecimal cardio) {
      this.cardio = cardio;
      return this;
    }

    public WorkoutBuilder withLifting(BigDecimal lifting) {
      this.lifting = lifting;
      return this;
    }

    public WorkoutBuilder withTotal(BigDecimal total) {
      this.total = total;
      return this;
    }

    public WorkoutBuilder withComment(String comment) {
      this.comment = comment;
      return this;
    }

    public Workout build() {
      Workout workout = new Workout();
      workout.date = this.date;
      workout.comment = this.comment;
      workout.cardio = this.cardio;
      workout.lifting = this.lifting;
      workout.total = this.total;
      return workout;
    }
  }
}
