package com.barnacle.lifting;

import static com.robertsanek.util.PostgresConnection.QUERY_TIMEOUT;
import static com.robertsanek.util.PostgresConnection.quote;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;

import com.barnacle.User;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.robertsanek.data.etl.remote.google.sheets.SheetsConnector;
import com.robertsanek.util.PostgresConnection;
import com.robertsanek.util.SecretProvider;
import com.robertsanek.util.SecretType;
import com.robertsanek.util.Unchecked;

public class LiftEtl implements Callable<Object> {

  private static final ImmutableList<ExerciseType> EXERCISE_TYPE_ORDER = ImmutableList.of(
      ExerciseType.DEADLIFT,
      ExerciseType.SQUAT,
      ExerciseType.LUNGE,
      ExerciseType.BENCH,
      ExerciseType.ROW,
      ExerciseType.OVERHEAD_PRESS
  );
  private static final String TABLE_NAME = "lifts";
  @Inject SecretProvider secretProvider;
  @Inject PostgresConnection postgresConnection;

  @Override
  public Object call() {
    List<List<Object>> rows =
        SheetsConnector.getSpreadsheetCells(secretProvider.getSecret(SecretType.LIFTING_SPREADSHEET_ID), "Lifts");
    List<Integer> dateIndexes = new ArrayList<>();
    for (int i = 0; i < rows.size(); i++) {
      if (rows.get(i).get(0).toString().contains("/")) {
        dateIndexes.add(i);
      }
    }
    List<List<List<Object>>> rowsSublistedByDay = new ArrayList<>();
    for (int j = 0; j < dateIndexes.size() - 1; j++) {
      rowsSublistedByDay.add(rows.subList(dateIndexes.get(j), dateIndexes.get(j + 1)));
    }
    List<LiftingDay> liftingDays = rowsSublistedByDay.stream()
        .map(liftingDayRows -> {
          HeavyOrEndurance heavyOrEndurance = HeavyOrEndurance.fromSetCount(liftingDayRows.size() - 1);
          String monthAndDay = liftingDayRows.get(0).get(0).toString();
          String maybeMonth = monthAndDay.substring(0, 2);
          if (maybeMonth.equals("11") || maybeMonth.equals("12")) {
            monthAndDay = monthAndDay + "/2017";
          } else {
            monthAndDay = monthAndDay + "/2018";
          }
          LocalDate day = LocalDate.parse(monthAndDay, DateTimeFormatter.ofPattern("M/d/uuuu", new Locale("en")));
          String maybeDescription1 = liftingDayRows.get(0).get(1).toString();
          String maybeDescription2 = liftingDayRows.get(0).get(4).toString();
          String description = maybeDescription1.contains("e") ? maybeDescription1 : maybeDescription2;

          final UpperOrLower upperOrLower = description.toUpperCase().contains("LOWER") ?
              UpperOrLower.LOWER : UpperOrLower.UPPER;
          final int upperAdjustment = upperOrLower == UpperOrLower.UPPER ? 3 : 0;

          Map<User, List<ExerciseDetails>> lifterToExercises = upperOrLower.getExerciseTypes().stream()
              .flatMap(type -> {
                int robIndex = 1 + upperOrLower.getExerciseTypes().indexOf(type) + upperAdjustment;
                int willIndex = robIndex + EXERCISE_TYPE_ORDER.size();
                int robWeight = toIntDefault0(liftingDayRows.get(0).get(robIndex).toString());
                int willWeight = willIndex < liftingDayRows.get(0).size() ?
                    toIntDefault0(liftingDayRows.get(0).get(willIndex).toString()) : 0;
                Stream.Builder<Pair<User, ExerciseDetails>> toReturn = Stream.builder();
                if (robWeight != 0) {
                  toReturn.add(Pair.of(User.ROB, new ExerciseDetails(
                      type,
                      robWeight,
                      getListOfReps(liftingDayRows, robIndex)
                  )));
                }
                if (willWeight != 0) {
                  toReturn.add(Pair.of(User.WILL, new ExerciseDetails(
                      type,
                      willWeight,
                      getListOfReps(liftingDayRows, willIndex)
                  )));
                }
                return toReturn.build();
              })
              .collect(Collectors.groupingBy(Pair::getLeft)).entrySet().stream()
              .collect(Collectors.toMap(Map.Entry::getKey,
                  entry -> entry.getValue().stream().map(Pair::getRight).toList()));

          return new LiftingDay(
              day,
              heavyOrEndurance,
              upperOrLower,
              lifterToExercises
          );
        })
        .toList();

    //    File csvFile = new File(System.getProperty("java.io.tmpdir"), String.format("%s.csv", TABLE_NAME));

    try (Connection connection = postgresConnection.getConnection(true);
         Statement statement = connection.createStatement()) {
      statement.setQueryTimeout((int) QUERY_TIMEOUT.getSeconds());
      statement.executeUpdate(String.format("DROP TABLE IF EXISTS %s", TABLE_NAME));
      statement.executeUpdate(String.format("CREATE TABLE %s (" +
              "at date," +
              "lifter varchar," +
              "exerciseType varchar," +
              "heavyOrEndurance varchar," +
              "weight integer," +
              "reps integer);",
          TABLE_NAME));
      //      statement.executeUpdate(String.format("\\copy %s FROM '%s' WITH (FORMAT csv);", TABLE_NAME, csvFile.getAbsolutePath()));
      List<String> toInsert = liftingDays
          .stream()
          .filter(liftingDay -> liftingDay.getRepsPerBodyPartByUser().size() > 0)
          .flatMap(liftingDay -> liftingDay.getRepsPerBodyPartByUser().entrySet().stream()
              .flatMap(repsPerBodyPartByLifter -> {
                User lifter = repsPerBodyPartByLifter.getKey();
                return repsPerBodyPartByLifter.getValue().stream()
                    .flatMap(exerciseDetails -> exerciseDetails.getRepsPerSet().stream()
                        .filter(reps -> reps > 0)
                        .map(repsPerSet -> {
                          final List<String> row = ImmutableList.<String>builder()
                              .add(quote(liftingDay.getDay().toString()))
                              .add(quote(lifter.toString()))
                              .add(quote(exerciseDetails.getExerciseType().toString()))
                              .add(quote(liftingDay.getHeavyOrEndurance().toString()))
                              .add(String.valueOf(exerciseDetails.getWeight()))
                              .add(repsPerSet.toString())
                              .build();
                          return String.format("(%s)", String.join(",", row));
                        }));
              }))
          .toList();

      Unchecked.run(() -> statement.executeUpdate(
          String.format("INSERT INTO %s VALUES %s", TABLE_NAME, String.join(",", toInsert))));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    //    try (final PrintWriter writer = Unchecked.get(() -> new PrintWriter(csvFile, "UTF-8"))) {
    //      writer.println(String.join(CSV_DELIMITER, "date", "lifter", "exerciseType", "heavyOrEndurance", "weight", "reps"));
    //    }
    return null;
  }

  private static List<Integer> getListOfReps(List<List<Object>> daysRows, int index) {
    return IntStream.range(1, daysRows.size())
        .mapToObj(setIndex -> {
          try {
            return toIntDefault0(daysRows.get(setIndex).get(index).toString());
          } catch (IndexOutOfBoundsException e) {
            return 0;
          }
        })
        .toList();
  }

  private static int toIntDefault0(Object o) {
    try {
      return Integer.parseInt(o.toString());
    } catch (NumberFormatException e) {
      return 0;
    }
  }

}
