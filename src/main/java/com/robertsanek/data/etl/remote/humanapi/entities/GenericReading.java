package com.robertsanek.data.etl.remote.humanapi.entities;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "nokia_readings")
@JsonIgnoreProperties(ignoreUnknown = true)
public class GenericReading {

  @Id
  @JsonProperty("id")
  private String id;
  @JsonProperty("timestamp")
  private ZonedDateTime timestamp;
  @JsonProperty("tzOffset")
  private String tzOffset;
  @JsonProperty("value")
  private Double value;
  @JsonProperty("unit")
  private String unit;
  @JsonProperty("source")
  private String source;
  @JsonProperty("createdAt")
  @Column(name = "created_at")
  private ZonedDateTime createdAt;
  @JsonProperty("updatedAt")
  @Column(name = "updated_at")
  private ZonedDateTime updatedAt;

  @JsonProperty("id")
  public String getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(String id) {
    this.id = id;
  }

  @JsonProperty("timestamp")
  public ZonedDateTime getTimestamp() {
    return timestamp;
  }

  @JsonProperty("timestamp")
  public void setTimestamp(ZonedDateTime timestamp) {
    this.timestamp = timestamp;
  }

  @JsonProperty("tzOffset")
  public String getTzOffset() {
    return tzOffset;
  }

  @JsonProperty("tzOffset")
  public void setTzOffset(String tzOffset) {
    this.tzOffset = tzOffset;
  }

  @JsonProperty("value")
  public Double getValue() {
    return value;
  }

  @JsonProperty("value")
  public void setValue(Double value) {
    this.value = value;
  }

  @JsonProperty("unit")
  public String getUnit() {
    return unit;
  }

  @JsonProperty("unit")
  public void setUnit(String unit) {
    this.unit = unit;
  }

  @JsonProperty("source")
  public String getSource() {
    return source;
  }

  @JsonProperty("source")
  public void setSource(String source) {
    this.source = source;
  }

  @JsonProperty("createdAt")
  public ZonedDateTime getCreatedAt() {
    return createdAt;
  }

  @JsonProperty("createdAt")
  public void setCreatedAt(ZonedDateTime createdAt) {
    this.createdAt = createdAt;
  }

  @JsonProperty("updatedAt")
  public ZonedDateTime getUpdatedAt() {
    return updatedAt;
  }

  @JsonProperty("updatedAt")
  public void setUpdatedAt(ZonedDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("id", id)
        .append("timestamp", timestamp)
        .append("tzOffset", tzOffset)
        .append("value", value)
        .append("unit", unit)
        .append("source", source)
        .append("createdAt", createdAt)
        .append("updatedAt", updatedAt)
        .toString();
  }
}