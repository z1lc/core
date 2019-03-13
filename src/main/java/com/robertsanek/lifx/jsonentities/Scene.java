package com.robertsanek.lifx.jsonentities;

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
    "uuid",
    "name",
    "account",
    "states",
    "created_at",
    "updated_at"
})
public class Scene {

  @JsonProperty("uuid")
  private String uuid;
  @JsonProperty("name")
  private String name;
  @JsonProperty("account")
  private Account account;
  @JsonProperty("states")
  private List<State> states = null;
  @JsonProperty("created_at")
  private Integer createdAt;
  @JsonProperty("updated_at")
  private Integer updatedAt;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<>();

  @JsonProperty("uuid")
  public String getUuid() {
    return uuid;
  }

  @JsonProperty("uuid")
  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  @JsonProperty("name")
  public String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  @JsonProperty("account")
  public Account getAccount() {
    return account;
  }

  @JsonProperty("account")
  public void setAccount(Account account) {
    this.account = account;
  }

  @JsonProperty("states")
  public List<State> getStates() {
    return states;
  }

  @JsonProperty("states")
  public void setStates(List<State> states) {
    this.states = states;
  }

  @JsonProperty("created_at")
  public Integer getCreatedAt() {
    return createdAt;
  }

  @JsonProperty("created_at")
  public void setCreatedAt(Integer createdAt) {
    this.createdAt = createdAt;
  }

  @JsonProperty("updated_at")
  public Integer getUpdatedAt() {
    return updatedAt;
  }

  @JsonProperty("updated_at")
  public void setUpdatedAt(Integer updatedAt) {
    this.updatedAt = updatedAt;
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
