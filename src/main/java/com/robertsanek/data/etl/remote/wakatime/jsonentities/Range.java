package com.robertsanek.data.etl.remote.wakatime.jsonentities;

import java.time.LocalDate;
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
    "date",
    "end",
    "start",
    "text",
    "timezone"
})
public class Range {

  @JsonProperty("date")
  private LocalDate date;
  @JsonProperty("end")
  private ZonedDateTime end;
  @JsonProperty("start")
  private ZonedDateTime start;
  @JsonProperty("text")
  private String text;
  @JsonProperty("timezone")
  private String timezone;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<>();

  @JsonProperty("date")
  public LocalDate getDate() {
    return date;
  }

  @JsonProperty("date")
  public void setDate(LocalDate date) {
    this.date = date;
  }

  @JsonProperty("end")
  public ZonedDateTime getEnd() {
    return end;
  }

  @JsonProperty("end")
  public void setEnd(ZonedDateTime end) {
    this.end = end;
  }

  @JsonProperty("start")
  public ZonedDateTime getStart() {
    return start;
  }

  @JsonProperty("start")
  public void setStart(ZonedDateTime start) {
    this.start = start;
  }

  @JsonProperty("text")
  public String getText() {
    return text;
  }

  @JsonProperty("text")
  public void setText(String text) {
    this.text = text;
  }

  @JsonProperty("timezone")
  public String getTimezone() {
    return timezone;
  }

  @JsonProperty("timezone")
  public void setTimezone(String timezone) {
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
