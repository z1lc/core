package com.robertsanek.data.etl.remote.fitbit.json;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "fitbit_sleep")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Sleep {

  @JsonProperty("awakeCount")
  @Column(name = "awake_count")
  private Long awakeCount;
  @JsonProperty("awakeDuration")
  @Column(name = "awake_duration")
  private Long awakeDuration;
  @JsonProperty("awakeningsCount")
  @Column(name = "awakenings_count")
  private Long awakeningsCount;
  @JsonProperty("dateOfSleep")
  @Column(name = "date_of_sleep")
  private String dateOfSleep;
  @JsonProperty("duration")
  @Column(name = "duration")
  private Long duration;
  @JsonProperty("efficiency")
  @Column(name = "efficiency")
  private Long efficiency;
  @JsonProperty("endTime")
  @Column(name = "end_time")
  private LocalDateTime endTime;
  @JsonProperty("isMainSleep")
  @Column(name = "is_main_sleep")
  private Boolean isMainSleep;
  @Id
  @JsonProperty("logId")
  @Column(name = "log_id")
  private Long logId;
  //    @JsonProperty("minuteData")
  //    private List<MinuteDatum> minuteData = null;
  @JsonProperty("minutesAfterWakeup")
  @Column(name = "mintes_after_wakeup")
  private Long minutesAfterWakeup;
  @JsonProperty("minutesAsleep")
  @Column(name = "minutes_asleep")
  private Long minutesAsleep;
  @JsonProperty("minutesAwake")
  @Column(name = "minutes_awake")
  private Long minutesAwake;
  @JsonProperty("minutesToFallAsleep")
  @Column(name = "minutes_to_fall_asleep")
  private Long minutesToFallAsleep;
  @JsonProperty("restlessCount")
  @Column(name = "restless_count")
  private Long restlessCount;
  @JsonProperty("restlessDuration")
  @Column(name = "restless_duration")
  private Long restlessDuration;
  @JsonProperty("startTime")
  @Column(name = "start_time")
  private LocalDateTime startTime;
  @JsonProperty("timeInBed")
  @Column(name = "time_in_bed")
  private Long timeInBed;

  @JsonProperty("awakeCount")
  public Long getAwakeCount() {
    return awakeCount;
  }

  @JsonProperty("awakeCount")
  public void setAwakeCount(Long awakeCount) {
    this.awakeCount = awakeCount;
  }

  @JsonProperty("awakeDuration")
  public Long getAwakeDuration() {
    return awakeDuration;
  }

  @JsonProperty("awakeDuration")
  public void setAwakeDuration(Long awakeDuration) {
    this.awakeDuration = awakeDuration;
  }

  @JsonProperty("awakeningsCount")
  public Long getAwakeningsCount() {
    return awakeningsCount;
  }

  @JsonProperty("awakeningsCount")
  public void setAwakeningsCount(Long awakeningsCount) {
    this.awakeningsCount = awakeningsCount;
  }

  @JsonProperty("dateOfSleep")
  public String getDateOfSleep() {
    return dateOfSleep;
  }

  @JsonProperty("dateOfSleep")
  public void setDateOfSleep(String dateOfSleep) {
    this.dateOfSleep = dateOfSleep;
  }

  @JsonProperty("duration")
  public Long getDuration() {
    return duration;
  }

  @JsonProperty("duration")
  public void setDuration(Long duration) {
    this.duration = duration;
  }

  @JsonProperty("efficiency")
  public Long getEfficiency() {
    return efficiency;
  }

  @JsonProperty("efficiency")
  public void setEfficiency(Long efficiency) {
    this.efficiency = efficiency;
  }

  @JsonProperty("endTime")
  public LocalDateTime getEndTime() {
    return endTime;
  }

  @JsonProperty("endTime")
  public void setEndTime(LocalDateTime endTime) {
    this.endTime = endTime;
  }

  @JsonProperty("isMainSleep")
  public Boolean getIsMainSleep() {
    return isMainSleep;
  }

  @JsonProperty("isMainSleep")
  public void setIsMainSleep(Boolean isMainSleep) {
    this.isMainSleep = isMainSleep;
  }

  @JsonProperty("logId")
  public Long getLogId() {
    return logId;
  }

  @JsonProperty("logId")
  public void setLogId(Long logId) {
    this.logId = logId;
  }

  //    @JsonProperty("minuteData")
  //    public List<MinuteDatum> getMinuteData() {
  //        return minuteData;
  //    }
  //
  //    @JsonProperty("minuteData")
  //    public void setMinuteData(List<MinuteDatum> minuteData) {
  //        this.minuteData = minuteData;
  //    }

  @JsonProperty("minutesAfterWakeup")
  public Long getMinutesAfterWakeup() {
    return minutesAfterWakeup;
  }

  @JsonProperty("minutesAfterWakeup")
  public void setMinutesAfterWakeup(Long minutesAfterWakeup) {
    this.minutesAfterWakeup = minutesAfterWakeup;
  }

  @JsonProperty("minutesAsleep")
  public Long getMinutesAsleep() {
    return minutesAsleep;
  }

  @JsonProperty("minutesAsleep")
  public void setMinutesAsleep(Long minutesAsleep) {
    this.minutesAsleep = minutesAsleep;
  }

  @JsonProperty("minutesAwake")
  public Long getMinutesAwake() {
    return minutesAwake;
  }

  @JsonProperty("minutesAwake")
  public void setMinutesAwake(Long minutesAwake) {
    this.minutesAwake = minutesAwake;
  }

  @JsonProperty("minutesToFallAsleep")
  public Long getMinutesToFallAsleep() {
    return minutesToFallAsleep;
  }

  @JsonProperty("minutesToFallAsleep")
  public void setMinutesToFallAsleep(Long minutesToFallAsleep) {
    this.minutesToFallAsleep = minutesToFallAsleep;
  }

  @JsonProperty("restlessCount")
  public Long getRestlessCount() {
    return restlessCount;
  }

  @JsonProperty("restlessCount")
  public void setRestlessCount(Long restlessCount) {
    this.restlessCount = restlessCount;
  }

  @JsonProperty("restlessDuration")
  public Long getRestlessDuration() {
    return restlessDuration;
  }

  @JsonProperty("restlessDuration")
  public void setRestlessDuration(Long restlessDuration) {
    this.restlessDuration = restlessDuration;
  }

  @JsonProperty("startTime")
  public LocalDateTime getStartTime() {
    return startTime;
  }

  @JsonProperty("startTime")
  public void setStartTime(LocalDateTime startTime) {
    this.startTime = startTime;
  }

  @JsonProperty("timeInBed")
  public Long getTimeInBed() {
    return timeInBed;
  }

  @JsonProperty("timeInBed")
  public void setTimeInBed(Long timeInBed) {
    this.timeInBed = timeInBed;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Sleep sleep = (Sleep) o;
    return Objects.equals(logId, sleep.logId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(logId);
  }
}
