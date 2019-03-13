package com.robertsanek.data.derived.anki;

import java.time.ZonedDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "anki_review_time_per_tag")
public class ReviewTimePerTag {

  @Id
  Long id;
  ZonedDateTime day;
  String tag;
  Long time_ms;

  public Long getId() {
    return id;
  }

  public ZonedDateTime getDay() {
    return day;
  }

  public String getTag() {
    return tag;
  }

  public Long getTime_ms() {
    return time_ms;
  }

  public static final class ReviewTimePerTagBuilder {

    Long id;
    ZonedDateTime day;
    String tag;
    Long time_ms;

    private ReviewTimePerTagBuilder() {}

    public static ReviewTimePerTagBuilder aReviewTimePerTag() {
      return new ReviewTimePerTagBuilder();
    }

    public ReviewTimePerTagBuilder withId(Long id) {
      this.id = id;
      return this;
    }

    public ReviewTimePerTagBuilder withDay(ZonedDateTime day) {
      this.day = day;
      return this;
    }

    public ReviewTimePerTagBuilder withTag(String tag) {
      this.tag = tag;
      return this;
    }

    public ReviewTimePerTagBuilder withTime_ms(Long time_ms) {
      this.time_ms = time_ms;
      return this;
    }

    public ReviewTimePerTag build() {
      ReviewTimePerTag reviewTimePerTag = new ReviewTimePerTag();
      reviewTimePerTag.day = this.day;
      reviewTimePerTag.tag = this.tag;
      reviewTimePerTag.time_ms = this.time_ms;
      reviewTimePerTag.id = this.id;
      return reviewTimePerTag;
    }
  }
}
