package com.robertsanek.data.derived.anki;

import static java.time.temporal.ChronoUnit.DAYS;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.robertsanek.data.etl.Etl;
import com.robertsanek.data.etl.SlowEtl;
import com.robertsanek.data.etl.local.sqllite.anki.Card;
import com.robertsanek.data.etl.local.sqllite.anki.Review;
import com.robertsanek.data.quality.anki.DataQualityBase;

@SlowEtl
public class HydratedReviewDeriver extends Etl<HydratedReview> {

  @Override
  public List<HydratedReview> getObjects() {
    final List<Review> reviews = DataQualityBase.getAllReviews();
    final List<Long> relevantDeckIds = DataQualityBase.getRelevantDeckIds(DataQualityBase.getAllDecks());
    final Map<Long, Card> cardsByCardId = DataQualityBase.toMap(DataQualityBase.getAllCards(), Card::getId);
    return reviews.stream()
        .filter(review -> {
          final Card card = cardsByCardId.get(review.getCard_id());
          return card != null && relevantDeckIds.contains(card.getDeck_id()) && card.getQueue() != Card.Queue.SUSPENDED;
        })
        .collect(Collectors.groupingBy(Review::getCard_id))
        .entrySet()
        .stream()
        .flatMap(groupedReview -> {
          final List<Review> orderedReviews = groupedReview.getValue().stream()
              .sorted(Comparator.comparing(Review::getCreated_at))
              .collect(Collectors.toList());
          final ZonedDateTime firstReview = orderedReviews.get(0).getCreated_at();
          return IntStream.range(0, orderedReviews.size())
              .mapToObj(i -> {
                final Review review = orderedReviews.get(i);
                Double skew = null;
                if (i > 0) {
                  final Review prevReview = orderedReviews.get(i - 1);
                  // We'll treat review that are new/relearn as having an interval of 1.
                  long interval = Math.max(1, prevReview.getInterval());
                  final long daysReviewDelayed =
                      DAYS.between(prevReview.getCreated_at().plusDays(interval), review.getCreated_at());
                  skew = (double) daysReviewDelayed / interval;
                }
                return HydratedReview.HydratedReviewBuilder.aHydratedReview()
                    .withReview_id(review.getId())
                    .withDays_since_first_review(DAYS.between(firstReview, review.getCreated_at()))
                    .withTotal_days_in_review(DAYS.between(firstReview, ZonedDateTime.now()))
                    .withSkew_at_review_time(skew)
                    .build();
              });
        })
        .collect(Collectors.toList());
  }
}
