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
    "artist",
    "@attr"
})
public class TopArtists {

  @JsonProperty("artist")
  private List<Artist> artist = null;
  @JsonProperty("@attr")
  private Attr_ attr;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("artist")
  public List<Artist> getArtist() {
    return artist;
  }

  @JsonProperty("artist")
  public void setArtist(List<Artist> artist) {
    this.artist = artist;
  }

  @JsonProperty("@attr")
  public Attr_ getAttr() {
    return attr;
  }

  @JsonProperty("@attr")
  public void setAttr(Attr_ attr) {
    this.attr = attr;
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
