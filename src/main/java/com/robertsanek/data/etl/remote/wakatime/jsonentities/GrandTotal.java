package com.robertsanek.data.etl.remote.wakatime.jsonentities;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "digital",
    "hours",
    "minutes",
    "text",
    "total_seconds"
})
public class GrandTotal {

  @JsonProperty("digital")
  private String digital;
  @JsonProperty("hours")
  private Integer hours;
  @JsonProperty("minutes")
  private Double minutes;
  @JsonProperty("text")
  private String text;
  @JsonProperty("total_seconds")
  private Integer totalSeconds;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<>();

  @JsonProperty("digital")
  public String getDigital() {
    return digital;
  }

  @JsonProperty("digital")
  public void setDigital(String digital) {
    this.digital = digital;
  }

  @JsonProperty("hours")
  public Integer getHours() {
    return hours;
  }

  @JsonProperty("hours")
  public void setHours(Integer hours) {
    this.hours = hours;
  }

  @JsonProperty("minutes")
  public Double getMinutes() {
    return minutes;
  }

  @JsonProperty("minutes")
  public void setMinutes(Double minutes) {
    this.minutes = minutes;
  }

  @JsonProperty("text")
  public String getText() {
    return text;
  }

  @JsonProperty("text")
  public void setText(String text) {
    this.text = text;
  }

  @JsonProperty("total_seconds")
  public Integer getTotalSeconds() {
    return totalSeconds;
  }

  @JsonProperty("total_seconds")
  public void setTotalSeconds(Integer totalSeconds) {
    this.totalSeconds = totalSeconds;
  }

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return this.additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
  }

}
