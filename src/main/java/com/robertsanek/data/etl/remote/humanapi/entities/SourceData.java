package com.robertsanek.data.etl.remote.humanapi.entities;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class SourceData {

  @JsonProperty("tracker")
  private Tracker tracker;
  @JsonProperty("caloriesBMR")
  private Integer caloriesBMR;
  @JsonProperty("activityCalories")
  private Integer activityCalories;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<>();

  @JsonProperty("tracker")
  public Tracker getTracker() {
    return tracker;
  }

  @JsonProperty("tracker")
  public void setTracker(Tracker tracker) {
    this.tracker = tracker;
  }

  @JsonProperty("caloriesBMR")
  public Integer getCaloriesBMR() {
    return caloriesBMR;
  }

  @JsonProperty("caloriesBMR")
  public void setCaloriesBMR(Integer caloriesBMR) {
    this.caloriesBMR = caloriesBMR;
  }

  @JsonProperty("activityCalories")
  public Integer getActivityCalories() {
    return activityCalories;
  }

  @JsonProperty("activityCalories")
  public void setActivityCalories(Integer activityCalories) {
    this.activityCalories = activityCalories;
  }

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return this.additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("tracker", tracker).append("caloriesBMR", caloriesBMR)
        .append("activityCalories", activityCalories).append("additionalProperties", additionalProperties).toString();
  }

}
