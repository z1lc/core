package com.robertsanek.data.derived.anki;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.robertsanek.data.etl.Etl;
import com.robertsanek.data.etl.local.sqllite.anki.Review;

public class BacklogByDayDeriver extends Etl<BacklogForDay> {

  public static final Map<LocalDate, Long> map = Maps.newHashMap();

  @Override
  public List<BacklogForDay> getObjects() {
    HydratedReviewDeriver.getReviewsByCardId()
        .forEach((key, value) -> {
          final List<Review> orderedReviews = value.stream()
              .sorted(Comparator.comparing(Review::getCreated_at))
              .collect(Collectors.toList());
          IntStream.rangeClosed(1, orderedReviews.size())
              .forEach(i -> {
                Review previousReview = orderedReviews.get(i - 1);
                LocalDate firstDate = previousReview.getCreated_at().toLocalDate();
                LocalDate secondDate = LocalDate.now();
                if (i < orderedReviews.size()) {
                  secondDate = orderedReviews.get(i).getCreated_at().toLocalDate();
                }
                long interval = Math.max(0, previousReview.getInterval());
                incrementBacklogsInMap(map, firstDate.plusDays(interval), secondDate);
              });
        });
    return map.entrySet().stream()
        .map(entry -> BacklogForDay.BacklogForDayBuilder.aBacklogForDay()
            .withDate(entry.getKey())
            .withCardsInBacklog(entry.getValue())
            .build())
        .collect(Collectors.toList());
  }

  @VisibleForTesting
  static void incrementBacklogsInMap(Map<LocalDate, Long> backlogMap, LocalDate reviewScheduledFor,
                              LocalDate actuallyReviewed) {
    while (reviewScheduledFor.isBefore(actuallyReviewed)) {
      backlogMap.put(reviewScheduledFor, backlogMap.getOrDefault(reviewScheduledFor, 0L) + 1);
      reviewScheduledFor = reviewScheduledFor.plusDays(1);
    }
  }
}
