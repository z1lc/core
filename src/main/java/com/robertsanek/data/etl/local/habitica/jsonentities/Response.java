package com.robertsanek.data.etl.local.habitica.jsonentities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

  @JsonProperty("success")
  private Boolean success;
  @JsonProperty("data")
  private List<JsonTask> data = null;
  @JsonProperty("notifications")
  private List<Object> notifications = null;
  @JsonProperty("userV")
  private Long userV;
  @JsonProperty("appVersion")
  private String appVersion;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<>();

  @JsonProperty("success")
  public Boolean getSuccess() {
    return success;
  }

  @JsonProperty("success")
  public void setSuccess(Boolean success) {
    this.success = success;
  }

  @JsonProperty("data")
  public List<JsonTask> getData() {
    return data;
  }

  @JsonProperty("data")
  public void setData(List<JsonTask> data) {
    this.data = data;
  }

  @JsonProperty("notifications")
  public List<Object> getNotifications() {
    return notifications;
  }

  @JsonProperty("notifications")
  public void setNotifications(List<Object> notifications) {
    this.notifications = notifications;
  }

  @JsonProperty("userV")
  public Long getUserV() {
    return userV;
  }

  @JsonProperty("userV")
  public void setUserV(Long userV) {
    this.userV = userV;
  }

  @JsonProperty("appVersion")
  public String getAppVersion() {
    return appVersion;
  }

  @JsonProperty("appVersion")
  public void setAppVersion(String appVersion) {
    this.appVersion = appVersion;
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
