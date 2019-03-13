package com.robertsanek.data.etl.remote.scrape.leetcode.jsonentities;

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
    "stat",
    "status",
    "difficulty",
    "paid_only",
    "is_favor",
    "frequency",
    "progress"
})
public class LCQuestion {

  @JsonProperty("stat")
  private Stat stat;
  @JsonProperty("status")
  private String status;
  @JsonProperty("difficulty")
  private Difficulty difficulty;
  @JsonProperty("paid_only")
  private Boolean paidOnly;
  @JsonProperty("is_favor")
  private Boolean isFavor;
  @JsonProperty("frequency")
  private Integer frequency;
  @JsonProperty("progress")
  private Integer progress;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<>();

  @JsonProperty("stat")
  public Stat getStat() {
    return stat;
  }

  @JsonProperty("stat")
  public void setStat(Stat stat) {
    this.stat = stat;
  }

  @JsonProperty("status")
  public String getStatus() {
    return status;
  }

  @JsonProperty("status")
  public void setStatus(String status) {
    this.status = status;
  }

  @JsonProperty("difficulty")
  public Difficulty getDifficulty() {
    return difficulty;
  }

  @JsonProperty("difficulty")
  public void setDifficulty(Difficulty difficulty) {
    this.difficulty = difficulty;
  }

  @JsonProperty("paid_only")
  public Boolean getPaidOnly() {
    return paidOnly;
  }

  @JsonProperty("paid_only")
  public void setPaidOnly(Boolean paidOnly) {
    this.paidOnly = paidOnly;
  }

  @JsonProperty("is_favor")
  public Boolean getIsFavor() {
    return isFavor;
  }

  @JsonProperty("is_favor")
  public void setIsFavor(Boolean isFavor) {
    this.isFavor = isFavor;
  }

  @JsonProperty("frequency")
  public Integer getFrequency() {
    return frequency;
  }

  @JsonProperty("frequency")
  public void setFrequency(Integer frequency) {
    this.frequency = frequency;
  }

  @JsonProperty("progress")
  public Integer getProgress() {
    return progress;
  }

  @JsonProperty("progress")
  public void setProgress(Integer progress) {
    this.progress = progress;
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
