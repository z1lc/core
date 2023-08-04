package com.robertsanek.data.derived.anki;

import static java.time.temporal.ChronoUnit.DAYS;

import java.time.LocalDate;
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
    return getReviewsByCardId()
        .entrySet()
        .parallelStream()
        .flatMap(groupedReview -> mapToHydratedReviews(groupedReview.getValue(), LocalDate.now()).stream())
        .toList();
  }

  public static List<HydratedReview> mapToHydratedReviews(List<Review> groupedReview, LocalDate today) {
    final List<Review> orderedReviews = groupedReview.stream()
        .sorted(Comparator.comparing(Review::getCreated_at))
        .toList();
    final LocalDate firstReview = orderedReviews.get(0).getCreated_at().toLocalDate();
    return IntStream.range(0, orderedReviews.size())
        .mapToObj(i -> {
          final Review review = orderedReviews.get(i);
          Double skew = null;
          Long daysReviewDelayed = null;
          Double effectiveEase = null;
          if (i > 0) {
            final Review prevReview = orderedReviews.get(i - 1);
            // New/relearn don't really have an interval, but the 'days in the future' they are scheduled is 0.
            // Here we use 0 and then take another max during the skew division to avoid a DivisionByZero exception.
            long interval = Math.max(0, prevReview.getInterval());
            LocalDate originallyScheduledForReviewOn =
                prevReview.getCreated_at().plusDays(interval).toLocalDate();
            LocalDate actuallyReviewedOn = review.getCreated_at().toLocalDate();
            daysReviewDelayed = DAYS.between(originallyScheduledForReviewOn, actuallyReviewedOn);
            long daysBetweenReviews = DAYS.between(prevReview.getCreated_at().toLocalDate(), actuallyReviewedOn);
            skew = (double) daysReviewDelayed / Math.max(1, interval);
            if (prevReview.getLast_interval() > 0) {
              effectiveEase = ((double) daysBetweenReviews) / prevReview.getLast_interval();
            }
          }
          return HydratedReview.HydratedReviewBuilder.aHydratedReview()
              .withReview_id(review.getId())
              .withDays_since_first_review(DAYS.between(firstReview, review.getCreated_at().toLocalDate()))
              .withTotal_days_in_review(DAYS.between(firstReview, today))
              .withNum_days_review_delayed(daysReviewDelayed)
              .withSkew_at_review_time(skew)
              .withEffective_ease_at_review_time(effectiveEase)
              .build();
        })
        .toList();
  }

  public static Map<Long, List<Review>> getReviewsByCardId() {
    final List<Review> reviews = DataQualityBase.getAllReviews();
    final List<Long> relevantDeckIds = DataQualityBase.getRelevantDeckIds(DataQualityBase.getAllDecks());
    final Map<Long, Card> cardsByCardId = DataQualityBase.getCardsByCardId();
    return reviews.parallelStream()
        .filter(review -> {
          final Card card = cardsByCardId.get(review.getCard_id());
          return card != null && relevantDeckIds.contains(card.getDeck_id()) && card.getQueue() != Card.Queue.SUSPENDED;
        })
        .collect(Collectors.groupingBy(Review::getCard_id));
  }
}
