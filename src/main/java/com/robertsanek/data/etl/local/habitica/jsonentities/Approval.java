package com.robertsanek.data.etl.local.habitica.jsonentities;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Approval {

  @JsonProperty("required")
  private Boolean required;
  @JsonProperty("approved")
  private Boolean approved;
  @JsonProperty("requested")
  private Boolean requested;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<>();

  @JsonProperty("required")
  public Boolean getRequired() {
    return required;
  }

  @JsonProperty("required")
  public void setRequired(Boolean required) {
    this.required = required;
  }

  @JsonProperty("approved")
  public Boolean getApproved() {
    return approved;
  }

  @JsonProperty("approved")
  public void setApproved(Boolean approved) {
    this.approved = approved;
  }

  @JsonProperty("requested")
  public Boolean getRequested() {
    return requested;
  }

  @JsonProperty("requested")
  public void setRequested(Boolean requested) {
    this.requested = requested;
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
