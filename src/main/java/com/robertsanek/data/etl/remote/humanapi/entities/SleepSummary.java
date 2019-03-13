package com.robertsanek.data.etl.remote.humanapi.entities;

import java.time.LocalDate;
import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "fitbit_sleep_summaries")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SleepSummary {

  @Id
  @JsonProperty("id")
  private String id;
  @JsonProperty("date")
  private LocalDate date;
  @JsonProperty("source")
  private String source;
  @JsonProperty("timeAsleep")
  @Column(name = "time_asleep_minutes")
  private Double timeAsleep;
  @JsonProperty("timeAwake")
  @Column(name = "time_awake_minutes")
  private Integer timeAwake;
  @JsonProperty("createdAt")
  private ZonedDateTime createdAt;
  @JsonProperty("updatedAt")
  private ZonedDateTime updatedAt;

  @JsonProperty("id")
  public String getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(String id) {
    this.id = id;
  }

  @JsonProperty("date")
  public LocalDate getDate() {
    return date;
  }

  @JsonProperty("date")
  public void setDate(LocalDate date) {
    this.date = date;
  }

  @JsonProperty("source")
  public String getSource() {
    return source;
  }

  @JsonProperty("source")
  public void setSource(String source) {
    this.source = source;
  }

  @JsonProperty("timeAsleep")
  public Double getTimeAsleep() {
    return timeAsleep;
  }

  @JsonProperty("timeAsleep")
  public void setTimeAsleep(Double timeAsleep) {
    this.timeAsleep = timeAsleep;
  }

  @JsonProperty("timeAwake")
  public Integer getTimeAwake() {
    return timeAwake;
  }

  @JsonProperty("timeAwake")
  public void setTimeAwake(Integer timeAwake) {
    this.timeAwake = timeAwake;
  }

  @JsonProperty("createdAt")
  public ZonedDateTime getCreatedAt() {
    return createdAt;
  }

  @JsonProperty("createdAt")
  public void setCreatedAt(ZonedDateTime createdAt) {
    this.createdAt = createdAt;
  }

  @JsonProperty("updatedAt")
  public ZonedDateTime getUpdatedAt() {
    return updatedAt;
  }

  @JsonProperty("updatedAt")
  public void setUpdatedAt(ZonedDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("id", id).append("date", date)
        .append("source", source).append("timeAsleep", timeAsleep).append("timeAwake", timeAwake)
        .append("createdAt", createdAt).append("updatedAt", updatedAt)
        .toString();
  }

}
