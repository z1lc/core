package com.robertsanek.data.quality.anki;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.ZonedDateTime;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.beust.jcommander.internal.Lists;
import com.robertsanek.data.derived.anki.HydratedReview;
import com.robertsanek.data.derived.anki.HydratedReviewDeriver;
import com.robertsanek.data.etl.local.sqllite.anki.Review;

public class HydratedReviewDeriverTest {

  @Test
  @Disabled("integration")
  public void integration() {
    List<HydratedReview> objects = new HydratedReviewDeriver().getObjects();
    System.out.println(objects);
  }

  @Test
  public void mapToHydratedReviews() {
    List<Review> reviews = Lists.newArrayList(
        Review.ReviewBuilder.aReview()
            .withId(1L)
            .withCreated_at(ZonedDateTime.now())
            .withLast_interval(-720L)    // 12 minutes
            .withInterval(-1500L)    //25 minutes
            .withEase(2L)
            .build(),
        Review.ReviewBuilder.aReview()
            .withId(2L)
            .withCreated_at(ZonedDateTime.now())
            .withLast_interval(-1500L)
            .withInterval(2L)  // graduating interval is 2 days
            .withEase(2L)
            .build(),
        Review.ReviewBuilder.aReview()
            .withId(3L)
            .withCreated_at(ZonedDateTime.now().plusDays(3))  // 3 days after last review; 1 day overdue
            .withLast_interval(2L)
            .withInterval(10L)
            .withEase(3L)
            .build(),
        Review.ReviewBuilder.aReview()
            .withId(4L)
            .withCreated_at(ZonedDateTime.now().plusDays(3 + 10))  // 10 days after last review; 0 days overdue
            .withLast_interval(10L)
            .withInterval(42L)
            .withEase(3L)
            .build(),
        Review.ReviewBuilder.aReview()
            .withId(5L)
            .withCreated_at(ZonedDateTime.now().plusDays(3 + 10 + 50))  // 50 days after last review; 8 days overdue
            .withLast_interval(42L)
            .withInterval(168L)
            .withEase(3L)
            .build()
    );
    List<HydratedReview> hydratedReviews =
        HydratedReviewDeriver.mapToHydratedReviews(
            reviews, reviews.get(reviews.size() - 1).getCreated_at().toLocalDate());
    HydratedReview firstLearn = hydratedReviews.get(0);
    HydratedReview secondLearnSlashGraduation = hydratedReviews.get(1);
    HydratedReview firstReview = hydratedReviews.get(2);
    HydratedReview secondReview = hydratedReviews.get(3);
    HydratedReview thirdReview = hydratedReviews.get(4);

    assertEquals(0L, firstLearn.getDays_since_first_review().longValue());
    assertEquals(0L, secondLearnSlashGraduation.getDays_since_first_review().longValue());
    assertEquals(3L, firstReview.getDays_since_first_review().longValue());
    assertEquals(13L, secondReview.getDays_since_first_review().longValue());
    assertEquals(63L, thirdReview.getDays_since_first_review().longValue());

    assertTrue(hydratedReviews.stream().allMatch(hr -> hr.getTotal_days_in_review() == 63L));

    assertNull(firstLearn.getNum_days_review_delayed());  // first learn can never be 'delayed'
    assertEquals(0L, secondLearnSlashGraduation.getNum_days_review_delayed().longValue());
    assertEquals(1L, firstReview.getNum_days_review_delayed().longValue());
    assertEquals(0L, secondReview.getNum_days_review_delayed().longValue());
    assertEquals(8L, thirdReview.getNum_days_review_delayed().longValue());

    assertNull(firstLearn.getSkew_at_review_time());  // first learn can never have skew
    assertEquals(0, secondLearnSlashGraduation.getSkew_at_review_time(), 0.001);
    assertEquals(1.0 / 2, firstReview.getSkew_at_review_time(), 0.001);
    assertEquals(0, secondReview.getSkew_at_review_time(), 0.001);
    assertEquals(8.0 / 42, thirdReview.getSkew_at_review_time(), 0.001);

    // learns + first review do not have have effective ease since the division would be negative
    assertNull(firstLearn.getEffective_ease_at_review_time());
    assertNull(secondLearnSlashGraduation.getEffective_ease_at_review_time());
    assertNull(firstReview.getEffective_ease_at_review_time());
    assertEquals(10.0 / 2, secondReview.getEffective_ease_at_review_time(), 0.001);
    assertEquals(50.0 / 10, thirdReview.getEffective_ease_at_review_time(), 0.001);
  }
}