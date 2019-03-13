package com.robertsanek.data.derived.anki;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
          long toGraduate = 0;
          long toMature = 0;
          long to90 = 0;
          boolean matured = false;
          boolean reached90 = false;
          for (Review review : reviewsPerCard) {
            if (review.getLast_interval() < 0) {
              toGraduate++;
            }
            if (review.getInterval() < 21) {
              toMature++;
            }
            if (review.getInterval() >= 21) {
              matured = true;
            }
            if (review.getInterval() < 90) {
              to90++;
            }
            if (review.getInterval() >= 90) {
              reached90 = true;
            }
          }
          return CardNewReps.CardNewRepsBuilder.aCardNewReps()
              .withCard_id(reviewsPerCard.get(0).getCard_id())
              .withReps_to_graduate(toGraduate)
              .withReps_to_mature(matured? toMature : null)
              .withReps_to_90d(reached90? to90 : null)
              .build();
        })
        .collect(Collectors.toList());
  }
}
