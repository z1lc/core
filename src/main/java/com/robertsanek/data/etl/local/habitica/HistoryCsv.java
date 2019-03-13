package com.robertsanek.data.etl.local.habitica;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonPropertyOrder(value = {"name", "id", "type", "date", "value"})
public class HistoryCsv {

  @JsonProperty("name")
  private String name;

  @JsonProperty("id")
  private String id;

  @JsonProperty("type")
  private String type;

  @JsonProperty("date")
  @JsonDeserialize(using = DateDeserializer.class)
  @JsonSerialize(using = DateSerializer.class)
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private LocalDateTime date;

  @JsonProperty("value")
  private Double value;

  public HistoryCsv() {
  }

  public HistoryCsv(String name, String id, String type, LocalDateTime date, Double value) {
    this.name = name;
    this.id = id;
    this.type = type;
    this.date = date;
    this.value = value;
  }

  @Override
  public String toString() {
    return "HistoryCsv{" +
        "name='" + name + '\'' +
        ", task_id='" + id + '\'' +
        ", type='" + type + '\'' +
        ", date=" + date +
        ", value=" + value +
        '}';
  }

  public String getName() {
    return name;
  }

  public String getId() {
    return id;
  }

  public String getType() {
    return type;
  }

  public LocalDateTime getDate() {
    return date;
  }

  public Double getValue() {
    return value;
  }
}
