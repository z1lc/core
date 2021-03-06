package com.robertsanek.data.etl.remote.scrape.toodledo;

import java.time.ZonedDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "toodledo_habits")
public class Habit {

  @JsonProperty("id")
  @Id
  private Long id;
  @JsonProperty("title")
  private String title;
  @JsonProperty("type")
  private Integer type;
  @JsonProperty("repeat")
  private Integer repeat;
  @JsonProperty("alarm")
  private Integer alarm;
  @JsonProperty("added")
  @JsonDeserialize(using = DateTimeDeserializer.class)
  private ZonedDateTime added;
  @JsonProperty("deleted")
  private Integer deleted;
  @JsonProperty("modified")
  @JsonDeserialize(using = DateTimeDeserializer.class)
  private ZonedDateTime modified;
  @JsonProperty("version")
  private Integer version;

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public Integer getType() {
    return type;
  }

  public Integer getRepeat() {
    return repeat;
  }

  public Integer getAlarm() {
    return alarm;
  }

  public ZonedDateTime getAdded() {
    return added;
  }

  public Integer getDeleted() {
    return deleted;
  }

  public ZonedDateTime getModified() {
    return modified;
  }

  public Integer getVersion() {
    return version;
  }
}
