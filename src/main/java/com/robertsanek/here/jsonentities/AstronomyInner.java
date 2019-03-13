package com.robertsanek.here.jsonentities;

import java.time.LocalTime;
import java.time.ZonedDateTime;
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
    "sunrise",
    "sunset",
    "moonrise",
    "moonset",
    "moonPhase",
    "moonPhaseDesc",
    "iconName",
    "city",
    "latitude",
    "longitude",
    "utcTime"
})
public class AstronomyInner {

  @JsonProperty("sunrise")
  private LocalTime sunrise;
  @JsonProperty("sunset")
  private LocalTime sunset;
  @JsonProperty("moonrise")
  private LocalTime moonrise;
  @JsonProperty("moonset")
  private LocalTime moonset;
  @JsonProperty("moonPhase")
  private Double moonPhase;
  @JsonProperty("moonPhaseDesc")
  private String moonPhaseDesc;
  @JsonProperty("iconName")
  private String iconName;
  @JsonProperty("city")
  private String city;
  @JsonProperty("latitude")
  private Double latitude;
  @JsonProperty("longitude")
  private Double longitude;
  @JsonProperty("utcTime")
  private ZonedDateTime utcTime;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<>();

  @JsonProperty("sunrise")
  public LocalTime getSunrise() {
    return sunrise;
  }

  @JsonProperty("sunrise")
  public void setSunrise(LocalTime sunrise) {
    this.sunrise = sunrise;
  }

  @JsonProperty("sunset")
  public LocalTime getSunset() {
    return sunset;
  }

  @JsonProperty("sunset")
  public void setSunset(LocalTime sunset) {
    this.sunset = sunset;
  }

  @JsonProperty("moonrise")
  public LocalTime getMoonrise() {
    return moonrise;
  }

  @JsonProperty("moonrise")
  public void setMoonrise(LocalTime moonrise) {
    this.moonrise = moonrise;
  }

  @JsonProperty("moonset")
  public LocalTime getMoonset() {
    return moonset;
  }

  @JsonProperty("moonset")
  public void setMoonset(LocalTime moonset) {
    this.moonset = moonset;
  }

  @JsonProperty("moonPhase")
  public Double getMoonPhase() {
    return moonPhase;
  }

  @JsonProperty("moonPhase")
  public void setMoonPhase(Double moonPhase) {
    this.moonPhase = moonPhase;
  }

  @JsonProperty("moonPhaseDesc")
  public String getMoonPhaseDesc() {
    return moonPhaseDesc;
  }

  @JsonProperty("moonPhaseDesc")
  public void setMoonPhaseDesc(String moonPhaseDesc) {
    this.moonPhaseDesc = moonPhaseDesc;
  }

  @JsonProperty("iconName")
  public String getIconName() {
    return iconName;
  }

  @JsonProperty("iconName")
  public void setIconName(String iconName) {
    this.iconName = iconName;
  }

  @JsonProperty("city")
  public String getCity() {
    return city;
  }

  @JsonProperty("city")
  public void setCity(String city) {
    this.city = city;
  }

  @JsonProperty("latitude")
  public Double getLatitude() {
    return latitude;
  }

  @JsonProperty("latitude")
  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  @JsonProperty("longitude")
  public Double getLongitude() {
    return longitude;
  }

  @JsonProperty("longitude")
  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }

  @JsonProperty("utcTime")
  public ZonedDateTime getUtcTime() {
    return utcTime;
  }

  @JsonProperty("utcTime")
  public void setUtcTime(ZonedDateTime utcTime) {
    this.utcTime = utcTime;
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
