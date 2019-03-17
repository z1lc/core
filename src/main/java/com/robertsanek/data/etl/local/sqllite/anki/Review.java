package com.robertsanek.data.etl.local.sqllite.anki;

import java.time.ZonedDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "anki_reviews")
public class Review {

  @Id
  Long id;
  ZonedDateTime created_at;
  Long card_id;
  Long ease;
  Long interval;
  Long last_interval;
  Long factor;
  Long time_ms;

  @Override
  public String toString() {
    return "Review{" +
        "id=" + id +
        ", created_at=" + created_at +
        ", card_id=" + card_id +
        ", ease=" + ease +
        ", interval=" + interval +
        ", last_interval=" + last_interval +
        ", factor=" + factor +
        ", time_ms=" + time_ms +
        '}';
  }

  public Long getId() {
    return id;
  }

  public ZonedDateTime getCreated_at() {
    return created_at;
  }

  public Long getCard_id() {
    return card_id;
  }

  public Long getEase() {
    return ease;
  }

  public Long getInterval() {
    return interval;
  }

  public Long getLast_interval() {
    return last_interval;
  }

  public Long getFactor() {
    return factor;
  }

  public Long getTime_ms() {
    return time_ms;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setCreated_at(ZonedDateTime created_at) {
    this.created_at = created_at;
  }

  public void setCard_id(Long card_id) {
    this.card_id = card_id;
  }

  public void setEase(Long ease) {
    this.ease = ease;
  }

  public void setInterval(Long interval) {
    this.interval = interval;
  }

  public void setLast_interval(Long last_interval) {
    this.last_interval = last_interval;
  }

  public void setFactor(Long factor) {
    this.factor = factor;
  }

  public void setTime_ms(Long time_ms) {
    this.time_ms = time_ms;
  }

  public static final class ReviewBuilder {

    Long id;
    ZonedDateTime created_at;
    Long card_id;
    Long ease;
    Long interval;
    Long last_interval;
    Long factor;
    Long time_ms;

    private ReviewBuilder() {}

    public static ReviewBuilder aReview() {
      return new ReviewBuilder();
    }

    public ReviewBuilder withId(Long id) {
      this.id = id;
      return this;
    }

    public ReviewBuilder withCreated_at(ZonedDateTime created_at) {
      this.created_at = created_at;
      return this;
    }

    public ReviewBuilder withCard_id(Long card_id) {
      this.card_id = card_id;
      return this;
    }

    public ReviewBuilder withEase(Long ease) {
      this.ease = ease;
      return this;
    }

    public ReviewBuilder withInterval(Long interval) {
      this.interval = interval;
      return this;
    }

    public ReviewBuilder withLast_interval(Long last_interval) {
      this.last_interval = last_interval;
      return this;
    }

    public ReviewBuilder withFactor(Long factor) {
      this.factor = factor;
      return this;
    }

    public ReviewBuilder withTime_ms(Long time_ms) {
      this.time_ms = time_ms;
      return this;
    }

    public Review build() {
      Review review = new Review();
      review.last_interval = this.last_interval;
      review.interval = this.interval;
      review.id = this.id;
      review.time_ms = this.time_ms;
      review.factor = this.factor;
      review.created_at = this.created_at;
      review.ease = this.ease;
      review.card_id = this.card_id;
      return review;
    }
  }
}
