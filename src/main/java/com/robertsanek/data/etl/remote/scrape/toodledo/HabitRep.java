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
@Table(name = "toodledo_habit_repetitions")
public class HabitRep {

  @JsonProperty("habit")
  private Long habit;
  @JsonProperty("date")
  @JsonDeserialize(using = SimpleDateDeserializer.class)
  private ZonedDateTime date;
  @JsonProperty("value")
  private Integer value;
  @JsonProperty("modified")
  @JsonDeserialize(using = DateTimeDeserializer.class)
  private ZonedDateTime modified;
  @JsonProperty("status")
  private String status;

  @Id
  private Long id;

  public void setId(Long id) {
    this.id = id;
  }
}
