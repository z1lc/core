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
@Table(name = "fitbit_activity_summaries")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActivitySummary {

  @Id
  @JsonProperty("id")
  private String id;
  @JsonProperty("date")
  private LocalDate date;
  @JsonProperty("duration")
  @Column(name = "active_seconds")
  private Integer duration;
  @JsonProperty("distance")
  @Column(name = "distance_meters")
  private Integer distance;
  @JsonProperty("steps")
  private Integer steps;
  @JsonProperty("calories")
  private Integer calories;
  @JsonProperty("source")
  private String source;
  @JsonProperty("vigorous")
  @Column(name = "vigorous_minutes")
  private Integer vigorous;
  @JsonProperty("moderate")
  @Column(name = "moderate_minutes")
  private Integer moderate;
  @JsonProperty("light")
  @Column(name = "light_minutes")
  private Integer light;
  @JsonProperty("sedentary")
  @Column(name = "sedentary_minutes")
  private Integer sedentary;
  @JsonProperty("createdAt")
  @Column(name = "created_at")
  private ZonedDateTime createdAt;
  @JsonProperty("updatedAt")
  @Column(name = "updated_at")
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

  @JsonProperty("duration")
  public Integer getDuration() {
    return duration;
  }

  @JsonProperty("duration")
  public void setDuration(Integer duration) {
    this.duration = duration;
  }

  @JsonProperty("distance")
  public Integer getDistance() {
    return distance;
  }

  @JsonProperty("distance")
  public void setDistance(Integer distance) {
    this.distance = distance;
  }

  @JsonProperty("steps")
  public Integer getSteps() {
    return steps;
  }

  @JsonProperty("steps")
  public void setSteps(Integer steps) {
    this.steps = steps;
  }

  @JsonProperty("calories")
  public Integer getCalories() {
    return calories;
  }

  @JsonProperty("calories")
  public void setCalories(Integer calories) {
    this.calories = calories;
  }

  @JsonProperty("source")
  public String getSource() {
    return source;
  }

  @JsonProperty("source")
  public void setSource(String source) {
    this.source = source;
  }

  @JsonProperty("vigorous")
  public Integer getVigorous() {
    return vigorous;
  }

  @JsonProperty("vigorous")
  public void setVigorous(Integer vigorous) {
    this.vigorous = vigorous;
  }

  @JsonProperty("moderate")
  public Integer getModerate() {
    return moderate;
  }

  @JsonProperty("moderate")
  public void setModerate(Integer moderate) {
    this.moderate = moderate;
  }

  @JsonProperty("light")
  public Integer getLight() {
    return light;
  }

  @JsonProperty("light")
  public void setLight(Integer light) {
    this.light = light;
  }

  @JsonProperty("sedentary")
  public Integer getSedentary() {
    return sedentary;
  }

  @JsonProperty("sedentary")
  public void setSedentary(Integer sedentary) {
    this.sedentary = sedentary;
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
        .append("duration", duration).append("distance", distance).append("steps", steps).append("calories", calories)
        .append("source", source).append("vigorous", vigorous).append("moderate", moderate).append("light", light)
        .append("sedentary", sedentary).append("createdAt", createdAt)
        .append("updatedAt", updatedAt)
        .toString();
  }

}
