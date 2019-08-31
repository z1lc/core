package com.barnacle;

import static com.robertsanek.util.PostgresConnection.QUERY_TIMEOUT;
import static com.robertsanek.util.PostgresConnection.quote;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Named;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import com.robertsanek.data.etl.remote.scrape.toodledo.Habit;
import com.robertsanek.data.etl.remote.scrape.toodledo.HabitEtl;
import com.robertsanek.data.etl.remote.scrape.toodledo.HabitRep;
import com.robertsanek.data.etl.remote.scrape.toodledo.HabitRepEtl;
import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;
import com.robertsanek.util.PostgresConnection;
import com.robertsanek.util.Unchecked;

public class LeetCodeToodledoTaskEtl {

  private static Log log = Logs.getLog(LeetCodeToodledoTaskEtl.class);
  private static final String TABLE_NAME = "toodledo_habit_reps";

  @Inject PostgresConnection postgresConnection;
  @Inject HabitEtl robHabitEtl;
  @Inject @Named("will") HabitEtl willHabitEtl;
  @Inject HabitRepEtl robHabitRepEtl;
  @Inject @Named("will") HabitRepEtl willHabitRepEtl;

  public void run() {
    log.info("Running LeetCode & Toodledo ETLs for Heroku Postgres...");
    ImmutableList<Pair<User, Habit>> habits = ImmutableList.<Pair<User, Habit>>builder()
        .addAll(Utils.addUser(robHabitEtl.getObjects(), User.ROB))
        .addAll(Utils.addUser(willHabitEtl.getObjects(), User.WILL))
        .build();

    List<Pair<User, Habit>> leetCodeOnly = habits.stream()
        .filter(habit -> habit.getRight().getTitle() != null && habit.getRight().getTitle().contains("LeetCode: 10m"))
        .collect(Collectors.toList());

    ImmutableList<Pair<User, HabitRep>> habitReps = ImmutableList.<Pair<User, HabitRep>>builder()
        .addAll(Utils.addUser(robHabitRepEtl.getObjects(), User.ROB))
        .addAll(Utils.addUser(willHabitRepEtl.getObjects(), User.WILL))
        .build();

    Unchecked.run(() -> Class.forName("org.postgresql.Driver"));
    try (Connection connection = postgresConnection.getConnection(true);
         Statement statement = connection.createStatement()) {
      statement.setQueryTimeout((int) QUERY_TIMEOUT.getSeconds());
      statement.executeUpdate(String.format("DROP TABLE IF EXISTS %s", TABLE_NAME));
      statement.executeUpdate(String.format("CREATE TABLE %s (" +
              "person varchar," +
              "day timestamp," +
              "completed bigint);",
          TABLE_NAME));
      List<String> toInsert = habitReps.stream()
          .filter(habitRep -> Iterables.getOnlyElement(leetCodeOnly.stream()
              .filter(habit -> habit.getLeft().equals(habitRep.getKey()))
              .collect(Collectors.toList())).getRight().getId().equals(habitRep.getRight().getHabit())
          )
          .map(userAndHabitRep -> {
            final List<String> row = ImmutableList.<String>builder()
                .add(quote(userAndHabitRep.getLeft()))
                .add(quote(userAndHabitRep.getRight().getDate().toLocalDateTime()
                    .withHour(0)
                    .withMinute(0)
                    .withSecond(0)
                    .withNano(0)))
                .add(quote(userAndHabitRep.getRight().getValue()))
                .build();
            return String.format("(%s)", String.join(",", row));
          })
          .collect(Collectors.toList());

      Unchecked.run(() -> statement.executeUpdate(
          String.format("INSERT INTO %s VALUES %s", TABLE_NAME, String.join(",", toInsert))));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    log.info("Successfully pushed LeetCode & Toodledo data to Heroku Postgres.");
  }

}
