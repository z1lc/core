package com.robertsanek.data.etl.local.habitica.jsonentities;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Repeat {

  @JsonProperty("m")
  private Boolean m;
  @JsonProperty("t")
  private Boolean t;
  @JsonProperty("w")
  private Boolean w;
  @JsonProperty("th")
  private Boolean th;
  @JsonProperty("f")
  private Boolean f;
  @JsonProperty("s")
  private Boolean s;
  @JsonProperty("su")
  private Boolean su;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<>();

  public boolean hasSomeRepetition() {
    return m || t || w || th || f || s || su;
  }

  public boolean isEveryDay() {
    return m && t && w && th && f && s && su;
  }

  public long getTotalReps() {
    return Lists.newArrayList(m, t, w, th, f, s, su).stream().mapToInt(bool -> bool ? 1 : 0).sum();
  }

  @JsonProperty("m")
  public Boolean getM() {
    return m;
  }

  @JsonProperty("m")
  public void setM(Boolean m) {
    this.m = m;
  }

  @JsonProperty("t")
  public Boolean getT() {
    return t;
  }

  @JsonProperty("t")
  public void setT(Boolean t) {
    this.t = t;
  }

  @JsonProperty("w")
  public Boolean getW() {
    return w;
  }

  @JsonProperty("w")
  public void setW(Boolean w) {
    this.w = w;
  }

  @JsonProperty("th")
  public Boolean getTh() {
    return th;
  }

  @JsonProperty("th")
  public void setTh(Boolean th) {
    this.th = th;
  }

  @JsonProperty("f")
  public Boolean getF() {
    return f;
  }

  @JsonProperty("f")
  public void setF(Boolean f) {
    this.f = f;
  }

  @JsonProperty("s")
  public Boolean getS() {
    return s;
  }

  @JsonProperty("s")
  public void setS(Boolean s) {
    this.s = s;
  }

  @JsonProperty("su")
  public Boolean getSu() {
    return su;
  }

  @JsonProperty("su")
  public void setSu(Boolean su) {
    this.su = su;
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
