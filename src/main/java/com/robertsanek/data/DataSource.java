package com.robertsanek.data;

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
    "id",
    "name",
    "description",
    "refresh_interval",
    "date_last_refresh",
    "is_dynamic"
})
public class DataSource {

  @JsonProperty("id")
  private String id;
  @JsonProperty("name")
  private String name;
  @JsonProperty("description")
  private String description;
  @JsonProperty("refresh_interval")
  private Integer refreshInterval;
  @JsonProperty("date_last_refresh")
  private String dateLastRefresh;
  @JsonProperty("is_dynamic")
  private Boolean isDynamic;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<>();

  @JsonProperty("id")
  public String getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(String id) {
    this.id = id;
  }

  @JsonProperty("name")
  public String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  @JsonProperty("description")
  public void setDescription(String description) {
    this.description = description;
  }

  @JsonProperty("refresh_interval")
  public Integer getRefreshInterval() {
    return refreshInterval;
  }

  @JsonProperty("refresh_interval")
  public void setRefreshInterval(Integer refreshInterval) {
    this.refreshInterval = refreshInterval;
  }

  @JsonProperty("date_last_refresh")
  public String getDateLastRefresh() {
    return dateLastRefresh;
  }

  @JsonProperty("date_last_refresh")
  public void setDateLastRefresh(String dateLastRefresh) {
    this.dateLastRefresh = dateLastRefresh;
  }

  @JsonProperty("is_dynamic")
  public Boolean getIsDynamic() {
    return isDynamic;
  }

  @JsonProperty("is_dynamic")
  public void setIsDynamic(Boolean isDynamic) {
    this.isDynamic = isDynamic;
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