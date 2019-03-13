package com.robertsanek.data.etl.remote.wakatime.jsonentities;

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
    "categories",
    "dependencies",
    "editors",
    "grand_total",
    "languages",
    "operating_systems",
    "projects",
    "range"
})
public class CodingInfoForDay {

  @JsonProperty("categories")
  private List<Category> categories = null;
  @JsonProperty("dependencies")
  private List<Dependency> dependencies = null;
  @JsonProperty("editors")
  private List<Editor> editors = null;
  @JsonProperty("grand_total")
  private GrandTotal grandTotal;
  @JsonProperty("languages")
  private List<Language> languages = null;
  @JsonProperty("operating_systems")
  private List<OperatingSystem> operatingSystems = null;
  @JsonProperty("projects")
  private List<Project> projects = null;
  @JsonProperty("range")
  private Range range;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<>();

  @JsonProperty("categories")
  public List<Category> getCategories() {
    return categories;
  }

  @JsonProperty("categories")
  public void setCategories(List<Category> categories) {
    this.categories = categories;
  }

  @JsonProperty("dependencies")
  public List<Dependency> getDependencies() {
    return dependencies;
  }

  @JsonProperty("dependencies")
  public void setDependencies(List<Dependency> dependencies) {
    this.dependencies = dependencies;
  }

  @JsonProperty("editors")
  public List<Editor> getEditors() {
    return editors;
  }

  @JsonProperty("editors")
  public void setEditors(List<Editor> editors) {
    this.editors = editors;
  }

  @JsonProperty("grand_total")
  public GrandTotal getGrandTotal() {
    return grandTotal;
  }

  @JsonProperty("grand_total")
  public void setGrandTotal(GrandTotal grandTotal) {
    this.grandTotal = grandTotal;
  }

  @JsonProperty("languages")
  public List<Language> getLanguages() {
    return languages;
  }

  @JsonProperty("languages")
  public void setLanguages(List<Language> languages) {
    this.languages = languages;
  }

  @JsonProperty("operating_systems")
  public List<OperatingSystem> getOperatingSystems() {
    return operatingSystems;
  }

  @JsonProperty("operating_systems")
  public void setOperatingSystems(List<OperatingSystem> operatingSystems) {
    this.operatingSystems = operatingSystems;
  }

  @JsonProperty("projects")
  public List<Project> getProjects() {
    return projects;
  }

  @JsonProperty("projects")
  public void setProjects(List<Project> projects) {
    this.projects = projects;
  }

  @JsonProperty("range")
  public Range getRange() {
    return range;
  }

  @JsonProperty("range")
  public void setRange(Range range) {
    this.range = range;
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
