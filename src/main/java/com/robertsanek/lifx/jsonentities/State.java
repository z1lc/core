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
    "selector",
    "power",
    "brightness",
    "color"
})
public class State {

  @JsonProperty("selector")
  private String selector;
  @JsonProperty("power")
  private String power;
  @JsonProperty("brightness")
  private Double brightness;
  @JsonProperty("color")
  private Color color;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<>();

  @JsonProperty("selector")
  public String getSelector() {
    return selector;
  }

  @JsonProperty("selector")
  public void setSelector(String selector) {
    this.selector = selector;
  }

  @JsonProperty("power")
  public String getPower() {
    return power;
  }

  @JsonProperty("power")
  public void setPower(String power) {
    this.power = power;
  }

  @JsonProperty("brightness")
  public Double getBrightness() {
    return brightness;
  }

  @JsonProperty("brightness")
  public void setBrightness(Double brightness) {
    this.brightness = brightness;
  }

  @JsonProperty("color")
  public Color getColor() {
    return color;
  }

  @JsonProperty("color")
  public void setColor(Color color) {
    this.color = color;
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
