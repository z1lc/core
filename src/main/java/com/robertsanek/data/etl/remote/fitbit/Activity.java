package com.robertsanek.data.etl.remote.fitbit;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "fitbit_activities")
public class Activity {

  @Id
  @JsonProperty("date")
  private LocalDate date;
  @Column(name = "lightly_active_minutes")
  private Integer lightlyActiveMinutes;
  @Column(name = "fairly_active_minutes")
  private Integer fairlyActiveMinutes;
  @Column(name = "very_active_minutes")
  private Integer veryActiveMinutes;
  @Column(name = "resting_heart_rate")
  private Integer restingHeartRate;

  public LocalDate getDate() {
    return date;
  }

  public Integer getLightlyActiveMinutes() {
    return lightlyActiveMinutes;
  }

  public Integer getFairlyActiveMinutes() {
    return fairlyActiveMinutes;
  }

  public Integer getVeryActiveMinutes() {
    return veryActiveMinutes;
  }

  public Integer getRestingHeartRate() {
    return restingHeartRate;
  }

  public static final class ActivityBuilder {

    Integer lightlyActiveMinutes;
    Integer fairlyActiveMinutes;
    Integer veryActiveMinutes;
    Integer restingHeartRate;
    private LocalDate date;

    private ActivityBuilder() {

    }

    public static ActivityBuilder anActivity() {
      return new ActivityBuilder();
    }

    public ActivityBuilder withDate(LocalDate date) {
      this.date = date;
      return this;
    }

    public ActivityBuilder withLightlyActiveMinutes(Integer lightlyActiveMinutes) {
      this.lightlyActiveMinutes = lightlyActiveMinutes;
      return this;
    }

    public ActivityBuilder withFairlyActiveMinutes(Integer fairlyActiveMinutes) {
      this.fairlyActiveMinutes = fairlyActiveMinutes;
      return this;
    }

    public ActivityBuilder withVeryActiveMinutes(Integer veryActiveMinutes) {
      this.veryActiveMinutes = veryActiveMinutes;
      return this;
    }

    public ActivityBuilder withRestingHeartRate(Integer restingHeartRate) {
      this.restingHeartRate = restingHeartRate;
      return this;
    }

    public Activity build() {
      Activity activity = new Activity();
      activity.fairlyActiveMinutes = this.fairlyActiveMinutes;
      activity.restingHeartRate = this.restingHeartRate;
      activity.date = this.date;
      activity.lightlyActiveMinutes = this.lightlyActiveMinutes;
      activity.veryActiveMinutes = this.veryActiveMinutes;
      return activity;
    }
  }
}
