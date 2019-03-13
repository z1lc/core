package com.robertsanek.passivekiva.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Location {

  @JsonProperty("country")
  private String country;
  @JsonProperty("town")
  private String town;

}
