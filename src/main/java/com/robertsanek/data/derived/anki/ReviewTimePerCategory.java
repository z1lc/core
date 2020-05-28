package com.robertsanek.data.derived.anki;

import java.time.ZonedDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "anki_review_time_per_category")
public class ReviewTimePerCategory {

  @Id
  Long id;
  ZonedDateTime day;
  String category;
  Long time_ms;

  public Long getId() {
    return id;
  }

  public ZonedDateTime getDay() {
    return day;
  }

  public String getCategory() {
    return category;
  }

  public Long getTime_ms() {
    return time_ms;
  }

  public static final class ReviewTimePerCategoryBuilder {

    Long id;
    ZonedDateTime day;
    String category;
    Long time_ms;

    private ReviewTimePerCategoryBuilder() {}

    public static ReviewTimePerCategoryBuilder aReviewTimePerTag() {
      return new ReviewTimePerCategoryBuilder();
    }

    public ReviewTimePerCategoryBuilder withId(Long id) {
      this.id = id;
      return this;
    }

    public ReviewTimePerCategoryBuilder withDay(ZonedDateTime day) {
      this.day = day;
      return this;
    }

    public ReviewTimePerCategoryBuilder withCategory(String tag) {
      this.category = tag;
      return this;
    }

    public ReviewTimePerCategoryBuilder withTime_ms(Long time_ms) {
      this.time_ms = time_ms;
      return this;
    }

    public ReviewTimePerCategory build() {
      ReviewTimePerCategory reviewTimePerCategory = new ReviewTimePerCategory();
      reviewTimePerCategory.day = this.day;
      reviewTimePerCategory.category = this.category;
      reviewTimePerCategory.time_ms = this.time_ms;
      reviewTimePerCategory.id = this.id;
      return reviewTimePerCategory;
    }
  }
}
