package com.robertsanek.data.derived.anki;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.robertsanek.data.etl.Etl;
import com.robertsanek.data.etl.local.sqllite.anki.Card;
import com.robertsanek.data.etl.local.sqllite.anki.Review;
import com.robertsanek.data.quality.anki.DataQualityBase;

public class NewRepsPerCardDeriver extends Etl<CardNewReps> {

  @Override
  public List<CardNewReps> getObjects() {
    List<Long> relevantDeckIds = DataQualityBase.getRelevantDeckIds(DataQualityBase.getAllDecks());
    List<Card> allCards = DataQualityBase.getAllCards();
    Set<Long> relevantCards = allCards.stream()
        .filter(card -> relevantDeckIds.stream().anyMatch(deck -> card.getDeck_id().equals(deck)))
        .filter(card -> card.getQueue() != Card.Queue.SUSPENDED)
        .map(Card::getId)
        .collect(Collectors.toCollection(HashSet::new));
    List<Review> reviews = DataQualityBase.getAllReviews();
    return reviews.stream()
        .collect(Collectors.groupingBy(Review::getCard_id))
        .values()
        .stream()
        .filter(reviewsPerCard -> relevantCards.contains(reviewsPerCard.get(0).getCard_id()))
        //only get cards that have graduated
        .filter(reviewsPerCard -> reviewsPerCard.stream().anyMatch(review -> review.getInterval() > 0))
        .map(reviewsPerCard -> {
          reviewsPerCard.sort(Comparator.comparing(Review::getCreated_at));
          boolean gotFirstReviewAfterGraduationCorrect = false;
          boolean foundFirstReviewAfterGraduation = false;
          Long graduatingInterval = null;
          for (int i = 0; i < reviewsPerCard.size(); i++) {
            Review review = reviewsPerCard.get(i);
            if (!foundFirstReviewAfterGraduation) {
              if (review.getLast_interval() == 1 &&
                  reviewsPerCard.get(i - 1).getCreated_at().toLocalDate().plusDays(1)
                      .equals(review.getCreated_at().toLocalDate())) {
                graduatingInterval = 1L;
                foundFirstReviewAfterGraduation = true;
                gotFirstReviewAfterGraduationCorrect = review.getEase() >= 3;
              } else if (review.getLast_interval() == 2 &&
                  reviewsPerCard.get(i - 1).getCreated_at().toLocalDate().plusDays(2)
                      .equals(review.getCreated_at().toLocalDate())) {
                graduatingInterval = 2L;
                foundFirstReviewAfterGraduation = true;
                gotFirstReviewAfterGraduationCorrect = review.getEase() >= 3;
              }
            }
          }
          Optional<Long> toGraduate = getNumReviewsTillFirstAbove(reviewsPerCard, 0);
          Optional<Long> toMature = getNumReviewsTillFirstAbove(reviewsPerCard, 21);
          Optional<Long> to90 = getNumReviewsTillFirstAbove(reviewsPerCard, 90);
          return CardNewReps.CardNewRepsBuilder.aCardNewReps()
              .withCard_id(reviewsPerCard.get(0).getCard_id())
              .withReps_to_graduate(toGraduate.orElse(null))
              .withReps_to_mature(toMature.orElse(null))
              .withReps_to_90d(to90.orElse(null))
              .withGraduating_interval(graduatingInterval)
              .withGotFirstReviewAfterGraduationCorrect(gotFirstReviewAfterGraduationCorrect)
              .withReviewedGraduationRepetitionOnTime(foundFirstReviewAfterGraduation)
              .build();
        })
        .collect(Collectors.toList());
  }

  public static Optional<Long> getNumReviewsTillFirstAbove(List<Review> reviews, long limit) {
    return IntStream.range(0, reviews.size())
        .filter(i -> reviews.get(i).getInterval() >= limit)
        .mapToObj(i -> (long) i + 1)
        .findFirst();
  }
}
