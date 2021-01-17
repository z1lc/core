package com.robertsanek.data.etl.remote.fitbit;

import java.time.LocalDate;
import java.util.Objects;

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
  @Column(name = "fat_burn_minutes")
  private Integer fatBurnMinutes;
  @Column(name = "cardio_minutes")
  private Integer cardioMinutes;
  @Column(name = "peak_minutes")
  private Integer peakMinutes;
  @Column(name = "active_zone_minutes")
  private Integer activeZoneMinutes;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Activity activity = (Activity) o;
    return Objects.equals(date, activity.date) &&
        Objects.equals(lightlyActiveMinutes, activity.lightlyActiveMinutes) &&
        Objects.equals(fairlyActiveMinutes, activity.fairlyActiveMinutes) &&
        Objects.equals(veryActiveMinutes, activity.veryActiveMinutes) &&
        Objects.equals(restingHeartRate, activity.restingHeartRate) &&
        Objects.equals(fatBurnMinutes, activity.fatBurnMinutes) &&
        Objects.equals(cardioMinutes, activity.cardioMinutes) &&
        Objects.equals(peakMinutes, activity.peakMinutes) &&
        Objects.equals(activeZoneMinutes, activity.activeZoneMinutes);
  }

  @Override
  public int hashCode() {
    return Objects
        .hash(date, lightlyActiveMinutes, fairlyActiveMinutes, veryActiveMinutes, restingHeartRate, fatBurnMinutes,
            cardioMinutes, peakMinutes, activeZoneMinutes);
  }

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

    private LocalDate date;
    private Integer lightlyActiveMinutes;
    private Integer fairlyActiveMinutes;
    private Integer veryActiveMinutes;
    private Integer restingHeartRate;
    private Integer fatBurnMinutes;
    private Integer cardioMinutes;
    private Integer peakMinutes;
    private Integer activeZoneMinutes;

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

    public ActivityBuilder withFatBurnMinutes(Integer fatBurnMinutes) {
      this.fatBurnMinutes = fatBurnMinutes;
      return this;
    }

    public ActivityBuilder withCardioMinutes(Integer cardioMinutes) {
      this.cardioMinutes = cardioMinutes;
      return this;
    }

    public ActivityBuilder withPeakMinutes(Integer peakMinutes) {
      this.peakMinutes = peakMinutes;
      return this;
    }

    public ActivityBuilder withActiveZoneMinutes(Integer activeZoneMinutes) {
      this.activeZoneMinutes = activeZoneMinutes;
      return this;
    }

    public Activity build() {
      Activity activity = new Activity();
      activity.cardioMinutes = this.cardioMinutes;
      activity.fairlyActiveMinutes = this.fairlyActiveMinutes;
      activity.restingHeartRate = this.restingHeartRate;
      activity.fatBurnMinutes = this.fatBurnMinutes;
      activity.peakMinutes = this.peakMinutes;
      activity.lightlyActiveMinutes = this.lightlyActiveMinutes;
      activity.date = this.date;
      activity.veryActiveMinutes = this.veryActiveMinutes;
      activity.activeZoneMinutes = this.activeZoneMinutes;
      return activity;
    }
  }
}
