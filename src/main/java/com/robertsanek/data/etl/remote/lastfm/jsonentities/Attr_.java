package com.robertsanek.data.etl.remote.lastfm.jsonentities;

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
    "page",
    "total",
    "user",
    "perPage",
    "totalPages"
})
public class Attr_ {

  @JsonProperty("page")
  private String page;
  @JsonProperty("total")
  private String total;
  @JsonProperty("user")
  private String user;
  @JsonProperty("perPage")
  private String perPage;
  @JsonProperty("totalPages")
  private String totalPages;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("page")
  public String getPage() {
    return page;
  }

  @JsonProperty("page")
  public void setPage(String page) {
    this.page = page;
  }

  @JsonProperty("total")
  public String getTotal() {
    return total;
  }

  @JsonProperty("total")
  public void setTotal(String total) {
    this.total = total;
  }

  @JsonProperty("user")
  public String getUser() {
    return user;
  }

  @JsonProperty("user")
  public void setUser(String user) {
    this.user = user;
  }

  @JsonProperty("perPage")
  public String getPerPage() {
    return perPage;
  }

  @JsonProperty("perPage")
  public void setPerPage(String perPage) {
    this.perPage = perPage;
  }

  @JsonProperty("totalPages")
  public String getTotalPages() {
    return totalPages;
  }

  @JsonProperty("totalPages")
  public void setTotalPages(String totalPages) {
    this.totalPages = totalPages;
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
