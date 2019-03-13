package com.robertsanek.data.derived.anki;

import static java.time.temporal.ChronoUnit.DAYS;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.robertsanek.data.etl.Etl;
import com.robertsanek.data.etl.SlowEtl;
import com.robertsanek.data.etl.local.sqllite.anki.Card;
import com.robertsanek.data.etl.local.sqllite.anki.Review;
import com.robertsanek.data.quality.anki.DataQualityBase;

@SlowEtl
public class DaysSinceReviewDeriver extends Etl<DaysSinceReview> {

  @Override
  public List<DaysSinceReview> getObjects() {
    List<Review> reviews = DataQualityBase.getAllReviews();
    List<Long> relevantDeckIds = DataQualityBase.getRelevantDeckIds(DataQualityBase.getAllDecks());
    Map<Long, Card> cardsByCardId = DataQualityBase.toMap(DataQualityBase.getAllCards(), Card::getId);
    return reviews.stream()
        .filter(review -> {
          Card card = cardsByCardId.get(review.getCard_id());
          return card != null && relevantDeckIds.contains(card.getDeck_id()) && card.getQueue() != Card.Queue.SUSPENDED;
        })
        .collect(Collectors.groupingBy(Review::getCard_id))
        .entrySet()
        .stream()
        .flatMap(groupedReview -> {
          List<Review> orderedReviews = groupedReview.getValue().stream()
              .sorted(Comparator.comparing(Review::getCreated_at))
              .collect(Collectors.toList());
          ZonedDateTime firstReview = orderedReviews.get(0).getCreated_at();
          return orderedReviews.stream()
              .map(review -> DaysSinceReview.DaysSinceReviewBuilder.aDaysSinceReview()
                  .withReview_id(review.getId())
                  .withDays_since_first_review(DAYS.between(firstReview, review.getCreated_at()))
                  .withTotal_days_in_review(DAYS.between(firstReview, ZonedDateTime.now()))
                  .build());
        })
        .collect(Collectors.toList());
  }
}
