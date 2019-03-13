package com.robertsanek.data.derived.anki;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "anki_days_since_review")
public class DaysSinceReview {

  @Id
  Long review_id;
  Long days_since_first_review;
  Long total_days_in_review;

  public Long getReview_id() {
    return review_id;
  }

  public Long getDays_since_first_review() {
    return days_since_first_review;
  }

  public Long getTotal_days_in_review() {
    return total_days_in_review;
  }

  public static final class DaysSinceReviewBuilder {

    Long review_id;
    Long days_since_first_review;
    Long total_days_in_review;

    private DaysSinceReviewBuilder() {}

    public static DaysSinceReviewBuilder aDaysSinceReview() {
      return new DaysSinceReviewBuilder();
    }

    public DaysSinceReviewBuilder withReview_id(Long review_id) {
      this.review_id = review_id;
      return this;
    }

    public DaysSinceReviewBuilder withDays_since_first_review(Long days_since_first_review) {
      this.days_since_first_review = days_since_first_review;
      return this;
    }

    public DaysSinceReviewBuilder withTotal_days_in_review(Long total_days_in_review) {
      this.total_days_in_review = total_days_in_review;
      return this;
    }

    public DaysSinceReview build() {
      DaysSinceReview daysSinceReview = new DaysSinceReview();
      daysSinceReview.total_days_in_review = this.total_days_in_review;
      daysSinceReview.review_id = this.review_id;
      daysSinceReview.days_since_first_review = this.days_since_first_review;
      return daysSinceReview;
    }

  }
}
