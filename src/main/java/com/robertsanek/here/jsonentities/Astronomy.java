package com.robertsanek.here.jsonentities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "astronomy",
    "country",
    "state",
    "city",
    "latitude",
    "longitude",
    "timezone"
})
public class Astronomy {

  @JsonProperty("astronomy")
  private List<AstronomyInner> astronomy = null;
  @JsonProperty("country")
  private String country;
  @JsonProperty("state")
  private String state;
  @JsonProperty("city")
  private String city;
  @JsonProperty("latitude")
  private Double latitude;
  @JsonProperty("longitude")
  private Double longitude;
  @JsonProperty("timezone")
  private Integer timezone;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<>();

  @JsonProperty("astronomy")
  public List<AstronomyInner> getAstronomyInner() {
    return astronomy;
  }

  @JsonProperty("astronomy")
  public void setAstronomyInner(List<AstronomyInner> astronomy) {
    this.astronomy = astronomy;
  }

  @JsonProperty("country")
  public String getCountry() {
    return country;
  }

  @JsonProperty("country")
  public void setCountry(String country) {
    this.country = country;
  }

  @JsonProperty("state")
  public String getState() {
    return state;
  }

  @JsonProperty("state")
  public void setState(String state) {
    this.state = state;
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

  @JsonProperty("timezone")
  public Integer getTimezone() {
    return timezone;
  }

  @JsonProperty("timezone")
  public void setTimezone(Integer timezone) {
    this.timezone = timezone;
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
