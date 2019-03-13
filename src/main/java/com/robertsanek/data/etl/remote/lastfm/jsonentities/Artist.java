package com.robertsanek.data.etl.remote.lastfm.jsonentities;

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
    "@attr",
    "mbid",
    "url",
    "playcount",
    "image",
    "name",
    "streamable"
})
public class Artist {

  @JsonProperty("@attr")
  private Attr attr;
  @JsonProperty("mbid")
  private String mbid;
  @JsonProperty("url")
  private String url;
  @JsonProperty("playcount")
  private String playcount;
  @JsonProperty("image")
  private List<Image> image = null;
  @JsonProperty("name")
  private String name;
  @JsonProperty("streamable")
  private String streamable;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("@attr")
  public Attr getAttr() {
    return attr;
  }

  @JsonProperty("@attr")
  public void setAttr(Attr attr) {
    this.attr = attr;
  }

  @JsonProperty("mbid")
  public String getMbid() {
    return mbid;
  }

  @JsonProperty("mbid")
  public void setMbid(String mbid) {
    this.mbid = mbid;
  }

  @JsonProperty("url")
  public String getUrl() {
    return url;
  }

  @JsonProperty("url")
  public void setUrl(String url) {
    this.url = url;
  }

  @JsonProperty("playcount")
  public String getPlaycount() {
    return playcount;
  }

  @JsonProperty("playcount")
  public void setPlaycount(String playcount) {
    this.playcount = playcount;
  }

  @JsonProperty("image")
  public List<Image> getImage() {
    return image;
  }

  @JsonProperty("image")
  public void setImage(List<Image> image) {
    this.image = image;
  }

  @JsonProperty("name")
  public String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  @JsonProperty("streamable")
  public String getStreamable() {
    return streamable;
  }

  @JsonProperty("streamable")
  public void setStreamable(String streamable) {
    this.streamable = streamable;
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
