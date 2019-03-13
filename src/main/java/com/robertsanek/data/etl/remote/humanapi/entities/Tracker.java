package com.robertsanek.data.etl.remote.humanapi.entities;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Tracker {

  @JsonProperty("calories")
  private String calories;
  @JsonProperty("steps")
  private String steps;
  @JsonProperty("distance")
  private Integer distance;
  @JsonProperty("floors")
  private Integer floors;
  @JsonProperty("elevation")
  private Double elevation;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<>();

  @JsonProperty("calories")
  public String getCalories() {
    return calories;
  }

  @JsonProperty("calories")
  public void setCalories(String calories) {
    this.calories = calories;
  }

  @JsonProperty("steps")
  public String getSteps() {
    return steps;
  }

  @JsonProperty("steps")
  public void setSteps(String steps) {
    this.steps = steps;
  }

  @JsonProperty("distance")
  public Integer getDistance() {
    return distance;
  }

  @JsonProperty("distance")
  public void setDistance(Integer distance) {
    this.distance = distance;
  }

  @JsonProperty("floors")
  public Integer getFloors() {
    return floors;
  }

  @JsonProperty("floors")
  public void setFloors(Integer floors) {
    this.floors = floors;
  }

  @JsonProperty("elevation")
  public Double getElevation() {
    return elevation;
  }

  @JsonProperty("elevation")
  public void setElevation(Double elevation) {
    this.elevation = elevation;
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
    return new ToStringBuilder(this).append("calories", calories).append("steps", steps).append("distance", distance)
        .append("floors", floors).append("elevation", elevation).append("additionalProperties", additionalProperties)
        .toString();
  }

}
