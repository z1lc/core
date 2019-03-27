package com.barnacle;

import static com.robertsanek.util.PostgresConnection.QUERY_TIMEOUT;
import static com.robertsanek.util.PostgresConnection.quote;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import com.google.common.collect.ImmutableList;
import com.robertsanek.data.etl.local.sqllite.anki.Review;
import com.robertsanek.data.etl.local.sqllite.anki.ReviewEtl;
import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;
import com.robertsanek.util.PostgresConnection;
import com.robertsanek.util.Unchecked;

public class AnkiEtl implements Callable<Object> {

  private static final String TABLE_NAME = "anki_reviews";
  private static final ZonedDateTime start = ZonedDateTime.of(2017, 11, 1, 0, 0, 0, 0, ZoneId.of("UTC"));
  private static final String ROB_NAME = "z1lc";
  private static final String WILL_NAME = "will";
  private static Log log = Logs.getLog(AnkiEtl.class);

  @Override
  public Object call() {
    log.info("Running Anki ETL for Heroku Postgres...");
    List<Pair<User, Review>> reviews = ImmutableList.<Pair<User, Review>>builder()
        .addAll(Unchecked.get(() -> new ReviewEtl() {
          @Override
          public String getProfileName() {
            return WILL_NAME;
          }
        }.getObjects()).stream()
            .map(review -> Pair.of(User.WILL, review))
            .collect(Collectors.toList()))
        .addAll(Unchecked.get(() -> new ReviewEtl() {
          @Override
          public String getProfileName() {
            return ROB_NAME;
          }
        }.getObjects()).stream()
            .map(review -> Pair.of(User.ROB, review))
            .collect(Collectors.toList()))
        .build().stream()
        .filter(userReviewPair -> userReviewPair.getRight().getCreated_at().isAfter(start))
        .collect(Collectors.toList());

    List<Triple<User, Review, Pair<Optional<Double>, Optional<Double>>>> reviews2 =
        reviews.stream().collect(Collectors.groupingBy(userReviewPair -> {
          ZonedDateTime createdAt = userReviewPair.getRight().getCreated_at();
          return Pair.of(userReviewPair.getLeft(), createdAt.withHour(0).withMinute(0).withSecond(0).withNano(0));
        })).entrySet().stream().map(
            pairListEntry -> {
              Review review = new Review();
              review.setCreated_at(pairListEntry.getKey().getRight());
              review.setTime_ms(
                  pairListEntry.getValue().stream().mapToLong(userReviewPair -> userReviewPair.getRight().getTime_ms())
                      .sum());
              int totalReviews = pairListEntry.getValue().size();
              Optional<Double> cardsPerMinute = Optional.empty();
              Optional<Double> percentCorrect = Optional.empty();
              if (totalReviews > 50) {
                cardsPerMinute =
                    Optional.of(totalReviews / (double) TimeUnit.MILLISECONDS.toMinutes(review.getTime_ms()));
                if (cardsPerMinute.orElseThrow() == Double.POSITIVE_INFINITY) {
                  cardsPerMinute = Optional.empty();
                }
                percentCorrect = Optional
                    .of((double) pairListEntry.getValue().stream().filter(reviewz -> reviewz.getRight().getEase() > 1)
                        .count() / totalReviews);
              }
              return Triple.of(pairListEntry.getKey().getLeft(), review, Pair.of(cardsPerMinute, percentCorrect));
            }).collect(Collectors.toList());

    Unchecked.run(() -> Class.forName("org.postgresql.Driver"));
    try (Connection connection = PostgresConnection.getConnection(true);
         Statement statement = connection.createStatement()) {
      statement.setQueryTimeout((int) QUERY_TIMEOUT.getSeconds());
      statement.executeUpdate(String.format("DROP TABLE IF EXISTS %s", TABLE_NAME));
      statement.executeUpdate(String.format("CREATE TABLE %s (" +
              "person varchar," +
              "created_at timestamp," +
              "cards_per_minute double precision," +
              "percent_correct double precision," +
              "time_ms bigint);",
          TABLE_NAME));
      List<String> toInsert = reviews2.stream().map(userAndReview -> {
        Review review = userAndReview.getMiddle();
        final List<String> row = ImmutableList.<String>builder()
            .add(quote(userAndReview.getLeft()))
            .add(quote(review.getCreated_at().toLocalDateTime()))
            .add(quote(userAndReview.getRight().getLeft().orElse(null)))
            .add(quote(userAndReview.getRight().getRight().orElse(null)))
            .add(quote(review.getTime_ms()))
            .build();
        return String.format("(%s)", String.join(",", row));
      })
          .collect(Collectors.toList());

      Unchecked.run(() -> statement.executeUpdate(
          String.format("INSERT INTO %s VALUES %s", TABLE_NAME, String.join(",", toInsert))));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    log.info("Successfully pushed Anki data to Heroku Postgres.");
    return null;
  }

}
