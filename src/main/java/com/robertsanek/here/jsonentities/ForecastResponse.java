package com.robertsanek.here.jsonentities;

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
    "astronomy",
    "feedCreation",
    "metric"
})
public class ForecastResponse {

  @JsonProperty("astronomy")
  private Astronomy astronomy;
  @JsonProperty("feedCreation")
  private ZonedDateTime feedCreation;
  @JsonProperty("metric")
  private Boolean metric;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<>();

  @JsonProperty("astronomy")
  public Astronomy getAstronomy() {
    return astronomy;
  }

  @JsonProperty("astronomy")
  public void setAstronomy(Astronomy astronomy) {
    this.astronomy = astronomy;
  }

  @JsonProperty("feedCreation")
  public ZonedDateTime getFeedCreation() {
    return feedCreation;
  }

  @JsonProperty("feedCreation")
  public void setFeedCreation(ZonedDateTime feedCreation) {
    this.feedCreation = feedCreation;
  }

  @JsonProperty("metric")
  public Boolean getMetric() {
    return metric;
  }

  @JsonProperty("metric")
  public void setMetric(Boolean metric) {
    this.metric = metric;
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
