package com.robertsanek.data.derived.anki;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableSet;
import com.robertsanek.data.etl.Etl;
import com.robertsanek.data.etl.local.sqllite.anki.Note;
import com.robertsanek.data.etl.local.sqllite.anki.Review;
import com.robertsanek.data.quality.anki.DataQualityBase;

public class ReviewTimePerTagDeriver extends Etl<ReviewTimePerTag> {

  public final ImmutableSet<String> EXCLUDED_TAGS = ImmutableSet.of("leech");
  private AtomicLong logicalClock = new AtomicLong(1);

  @Override
  public List<ReviewTimePerTag> getObjects() {
    List<Review> reviews = DataQualityBase.getAllReviews();
    return reviews.stream()
        .collect(Collectors.groupingBy(review -> review.getCreated_at()
            .withNano(0)
            .withSecond(0)
            .withMinute(0)
            .withHour(0)))
        .entrySet()
        .stream()
        .flatMap(pair -> {
          ZonedDateTime day = pair.getKey();
          List<Review> reviewsOnDay = pair.getValue();
          return reviewsOnDay.stream()
              .collect(Collectors.groupingBy(this::getTags))
              .entrySet()
              .stream()
              .flatMap(entry -> {
                List<Review> reviewsForTagsOnDay = entry.getValue();
                List<String> tags = DataQualityBase.splitCsvIntoCommaSeparatedList(entry.getKey());
                if (tags.size() == 0) {
                  tags.add("");
                }
                return tags.stream()
                    .filter(tag -> !EXCLUDED_TAGS.contains(tag))
                    .map(tag -> ReviewTimePerTag.ReviewTimePerTagBuilder.aReviewTimePerTag()
                        .withId(logicalClock.getAndIncrement())
                        .withDay(day)
                        .withTag(tag)
                        .withTime_ms(reviewsForTagsOnDay.stream().mapToLong(Review::getTime_ms).sum())
                        .build());
              });
        })
        .collect(Collectors.toList());
  }

  private String getTags(Review review) {
    return Optional.ofNullable(DataQualityBase.getCardsByCardId().get(review.getCard_id()))
        .map(card -> DataQualityBase.getNotesByNoteId().get(card.getNote_id()))
        .map(Note::getTags)
        .orElse("");
  }

}
