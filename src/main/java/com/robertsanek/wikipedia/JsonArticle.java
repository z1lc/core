package com.robertsanek.wikipedia;

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
    "article",
    "views",
    "rank"
})
public class JsonArticle {

  @JsonProperty("article")
  private String article;
  @JsonProperty("views")
  private Long views;
  @JsonProperty("rank")
  private Integer rank;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<>();

  @Override
  public String toString() {
    return "JsonArticle{" +
        "article='" + article + '\'' +
        ", views=" + views +
        ", rank=" + rank +
        '}';
  }

  @JsonProperty("article")
  public String getArticle() {
    return article;
  }

  @JsonProperty("article")
  public void setArticle(String article) {
    this.article = article;
  }

  @JsonProperty("views")
  public Long getViews() {
    return views;
  }

  @JsonProperty("views")
  public void setViews(Long views) {
    this.views = views;
  }

  @JsonProperty("rank")
  public Integer getRank() {
    return rank;
  }

  @JsonProperty("rank")
  public void setRank(Integer rank) {
    this.rank = rank;
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