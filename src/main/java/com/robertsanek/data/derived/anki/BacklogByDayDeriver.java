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

  // Default map that looks at the traditional 'backlog' definition -- what would the Anki UI
  // report if I were to go back in time and take a look at the 'due' count for a given day?
  public static final Map<LocalDate, Long> todayMap = Maps.newHashMap();
  // Map that looks 7 days into the future -- if I were to create a filtered deck
  // that said `prop:due<=7` for a given day, what would the 'due' count be?
  public static final Map<LocalDate, Long> sevenDayMap = Maps.newHashMap();
  public static final Map<LocalDate, Long> thirtyDayMap = Maps.newHashMap();

  @Override
  public List<BacklogForDay> getObjects() {
    HydratedReviewDeriver.getReviewsByCardId()
        .forEach((cardId, reviews) -> {
          final List<Review> orderedReviews = reviews.stream()
              .sorted(Comparator.comparing(Review::getCreated_at))
              .toList();
          IntStream.rangeClosed(1, orderedReviews.size())
              .forEach(i -> {
                Review previousReview = orderedReviews.get(i - 1);
                LocalDate previousReviewAt = previousReview.getCreated_at().toLocalDate();
                LocalDate thisReviewAt = LocalDate.now().plusDays(1);
                if (i < orderedReviews.size()) {
                  thisReviewAt = orderedReviews.get(i).getCreated_at().toLocalDate();
                }
                long interval = Math.max(0, previousReview.getInterval());
                incrementBacklogsInMap(todayMap, previousReviewAt.plusDays(interval), thisReviewAt);

                long sevenDayAdjustment = Math.min(interval, 7);
                incrementBacklogsInMap(sevenDayMap, previousReviewAt.plusDays(interval).minusDays(sevenDayAdjustment), thisReviewAt);
                long thirtyDayAdjustment = Math.min(interval, 30);
                incrementBacklogsInMap(thirtyDayMap, previousReviewAt.plusDays(interval).minusDays(thirtyDayAdjustment), thisReviewAt);
              });
        });
    return todayMap.entrySet().stream()
        .map(entry -> BacklogForDay.BacklogForDayBuilder.aBacklogForDay()
            .withDate(entry.getKey())
            .withCardsInBacklog(entry.getValue())
            .withCardsInBacklogSevenDays(sevenDayMap.get(entry.getKey()))
            .withCardsInBacklogThirtyDays(thirtyDayMap.get(entry.getKey()))
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
