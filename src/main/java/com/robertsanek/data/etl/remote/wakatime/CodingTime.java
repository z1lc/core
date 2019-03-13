package com.robertsanek.data.etl.remote.wakatime;

import java.time.ZonedDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "coding_time")
public class CodingTime {

  @Id
  public Long id;
  public ZonedDateTime date;
  public String language;
  public Long seconds;

  public Long getId() {
    return id;
  }

  public ZonedDateTime getDate() {
    return date;
  }

  public String getLanguage() {
    return language;
  }

  public Long getSeconds() {
    return seconds;
  }

  public static final class CodingTimeBuilder {

    public Long id;
    public ZonedDateTime date;
    public String language;
    public Long seconds;

    private CodingTimeBuilder() {}

    public static CodingTimeBuilder aCodingTime() {
      return new CodingTimeBuilder();
    }

    public CodingTimeBuilder withId(Long id) {
      this.id = id;
      return this;
    }

    public CodingTimeBuilder withDate(ZonedDateTime date) {
      this.date = date;
      return this;
    }

    public CodingTimeBuilder withLanguage(String language) {
      this.language = language;
      return this;
    }

    public CodingTimeBuilder withSeconds(Long seconds) {
      this.seconds = seconds;
      return this;
    }

    public CodingTime build() {
      CodingTime codingTime = new CodingTime();
      codingTime.date = this.date;
      codingTime.id = this.id;
      codingTime.seconds = this.seconds;
      codingTime.language = this.language;
      return codingTime;
    }
  }
}
