package com.robertsanek.util;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Secret {

  @JsonProperty("type")
  private SecretType type;
  @JsonProperty("secret")
  private String secret;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<>();

  public Secret() {
  }

  public Secret(SecretType type, String secret, Map<String, Object> additionalProperties) {
    this.type = type;
    this.secret = secret;
    this.additionalProperties = additionalProperties;
  }

  @JsonProperty("type")
  public SecretType getType() {
    return type;
  }

  @JsonProperty("name")
  public void setType(SecretType type) {
    this.type = type;
  }

  @JsonProperty("secret")
  public String getSecret() {
    return secret;
  }

  @JsonProperty("secret")
  public void setSecret(String secret) {
    this.secret = secret;
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