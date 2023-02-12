package com.robertsanek.data.quality.anki;


import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.beust.jcommander.internal.Lists;
import com.robertsanek.data.derived.anki.CardNewReps;
import com.robertsanek.data.derived.anki.NewRepsPerCardDeriver;
import com.robertsanek.data.etl.local.sqllite.anki.Review;

public class NewRepsPerCardDeriverTest {

  @Test
  @Disabled("integration")
  public void integration() {
    List<CardNewReps> objects = new NewRepsPerCardDeriver().getObjects().stream()
        .filter(nr -> DataQualityBase.cardByCardId.get(nr.getCard_id())
            .getCreated_at().isAfter(ZonedDateTime.now().minusDays(30)))
        .toList();
    long sum = objects.stream()
        .mapToLong(CardNewReps::getReps_to_graduate)
        .sum();
    long count = objects.size();
    System.out.println((double) sum / count);
  }

  @Test
  @Disabled("integration")
  public void integration2() {
    List<CardNewReps> ret = new NewRepsPerCardDeriver().getObjects();
    long correct = ret.stream()
        .filter(CardNewReps::isGotFirstReviewAfterGraduationCorrect)
        .filter(card -> card.getGraduating_interval() == 2)
        .count();
    System.out.println((double) correct);
  }

  @Test
  public void getNumReviewsTillFirstAbove() {
    Assertions.assertEquals(Optional.empty(),
        NewRepsPerCardDeriver.getNumReviewsTillFirstAbove(Lists.newArrayList(), 21));

    List<Review> reviews = Lists.newArrayList(
        // example comes from card id # 1599061445876
        Review.ReviewBuilder.aReview().withInterval(-1500L).build(),
        Review.ReviewBuilder.aReview().withInterval(2L).build(),
        Review.ReviewBuilder.aReview().withInterval(7L).build(),
        Review.ReviewBuilder.aReview().withInterval(24L).build(),
        Review.ReviewBuilder.aReview().withInterval(103L).build(),
        Review.ReviewBuilder.aReview().withInterval(431L).build(),
        Review.ReviewBuilder.aReview().withInterval(1666L).build()
    );

    Assertions.assertEquals(Optional.of(2L), NewRepsPerCardDeriver.getNumReviewsTillFirstAbove(reviews, 0));
    Assertions.assertEquals(Optional.of(4L), NewRepsPerCardDeriver.getNumReviewsTillFirstAbove(reviews, 21));
    Assertions.assertEquals(Optional.of(5L), NewRepsPerCardDeriver.getNumReviewsTillFirstAbove(reviews, 90));
  }

}