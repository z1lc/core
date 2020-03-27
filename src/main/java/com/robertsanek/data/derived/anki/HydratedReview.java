package com.robertsanek.data.derived.anki;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "anki_hydrated_reviews")
public class HydratedReview {

  @Id
  Long review_id;
  Long days_since_first_review;
  Long total_days_in_review;
  Long num_days_review_delayed;
  Double skew_at_review_time;
  Double effective_ease_at_review_time;

  public Long getReview_id() {
    return review_id;
  }

  public Long getDays_since_first_review() {
    return days_since_first_review;
  }

  public Long getTotal_days_in_review() {
    return total_days_in_review;
  }

  public Long getNum_days_review_delayed() {
    return num_days_review_delayed;
  }

  public Double getSkew_at_review_time() {
    return skew_at_review_time;
  }

  public Double getEffective_ease_at_review_time() {
    return effective_ease_at_review_time;
  }

  public static final class HydratedReviewBuilder {

    Long review_id;
    Long days_since_first_review;
    Long total_days_in_review;
    Long num_days_review_delayed;
    Double skew_at_review_time;
    Double effective_ease_at_review_time;

    private HydratedReviewBuilder() {}

    public static HydratedReviewBuilder aHydratedReview() { return new HydratedReviewBuilder(); }

    public HydratedReviewBuilder withReview_id(Long review_id) {
      this.review_id = review_id;
      return this;
    }

    public HydratedReviewBuilder withDays_since_first_review(Long days_since_first_review) {
      this.days_since_first_review = days_since_first_review;
      return this;
    }

    public HydratedReviewBuilder withTotal_days_in_review(Long total_days_in_review) {
      this.total_days_in_review = total_days_in_review;
      return this;
    }

    public HydratedReviewBuilder withNum_days_review_delayed(Long num_days_review_delayed) {
      this.num_days_review_delayed = num_days_review_delayed;
      return this;
    }

    public HydratedReviewBuilder withSkew_at_review_time(Double skew_at_review_time) {
      this.skew_at_review_time = skew_at_review_time;
      return this;
    }

    public HydratedReviewBuilder withEffective_ease_at_review_time(Double effective_ease_at_review_time) {
      this.effective_ease_at_review_time = effective_ease_at_review_time;
      return this;
    }

    public HydratedReview build() {
      HydratedReview hydratedReview = new HydratedReview();
      hydratedReview.days_since_first_review = this.days_since_first_review;
      hydratedReview.effective_ease_at_review_time = this.effective_ease_at_review_time;
      hydratedReview.total_days_in_review = this.total_days_in_review;
      hydratedReview.num_days_review_delayed = this.num_days_review_delayed;
      hydratedReview.skew_at_review_time = this.skew_at_review_time;
      hydratedReview.review_id = this.review_id;
      return hydratedReview;
    }
  }
}
