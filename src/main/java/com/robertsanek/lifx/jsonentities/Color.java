package com.robertsanek.lifx.jsonentities;

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
    "hue",
    "saturation",
    "kelvin"
})
public class Color {

  @JsonProperty("hue")
  private Integer hue;
  @JsonProperty("saturation")
  private Double saturation;
  @JsonProperty("kelvin")
  private Integer kelvin;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<>();

  @JsonProperty("hue")
  public Integer getHue() {
    return hue;
  }

  @JsonProperty("hue")
  public void setHue(Integer hue) {
    this.hue = hue;
  }

  @JsonProperty("saturation")
  public Double getSaturation() {
    return saturation;
  }

  @JsonProperty("saturation")
  public void setSaturation(Double saturation) {
    this.saturation = saturation;
  }

  @JsonProperty("kelvin")
  public Integer getKelvin() {
    return kelvin;
  }

  @JsonProperty("kelvin")
  public void setKelvin(Integer kelvin) {
    this.kelvin = kelvin;
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
