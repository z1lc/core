package com.barnacle;

import static com.robertsanek.util.PostgresConnection.QUERY_TIMEOUT;
import static com.robertsanek.util.PostgresConnection.quote;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.robertsanek.data.etl.remote.scrape.toodledo.Habit;
import com.robertsanek.data.etl.remote.scrape.toodledo.HabitEtl;
import com.robertsanek.data.etl.remote.scrape.toodledo.HabitRep;
import com.robertsanek.data.etl.remote.scrape.toodledo.HabitRepEtl;
import com.robertsanek.util.CommonProvider;
import com.robertsanek.util.PostgresConnection;
import com.robertsanek.util.SecretType;
import com.robertsanek.util.Unchecked;

public class LeetCodeToodledoTaskEtl {

  private static final String TABLE_NAME = "toodledo_habit_reps";

  public void run() {
    ImmutableList<Pair<User, Habit>> habits = ImmutableList.<Pair<User, Habit>>builder()
        .addAll(Utils.addUser(new HabitEtl().getObjects(), User.ROB))
        .addAll(Utils.addUser(new HabitEtl() {
          @Override
          public String getUsername() {
            return CommonProvider.getSecret(SecretType.TOODLEDO_WILL_USERNAME);
          }

          @Override
          public String getPassword() {
            return CommonProvider.getSecret(SecretType.TOODLEDO_WILL_PASSWORD);
          }
        }.getObjects(), User.WILL))
        .build();

    List<Pair<User, Habit>> leetCodeOnly = habits.stream()
        .filter(habit -> habit.getRight().getTitle() != null && habit.getRight().getTitle().contains("LeetCode: 10m"))
        .collect(Collectors.toList());

    ImmutableList<Pair<User, HabitRep>> habitReps = ImmutableList.<Pair<User, HabitRep>>builder()
        .addAll(Utils.addUser(new HabitRepEtl().getObjects(), User.ROB))
        .addAll(Utils.addUser(new HabitRepEtl() {
          @Override
          public String getUsername() {
            return CommonProvider.getSecret(SecretType.TOODLEDO_WILL_USERNAME);
          }

          @Override
          public String getPassword() {
            return CommonProvider.getSecret(SecretType.TOODLEDO_WILL_PASSWORD);
          }
        }.getObjects(), User.WILL))
        .build();

    Unchecked.run(() -> Class.forName("org.postgresql.Driver"));
    try (Connection connection = PostgresConnection.getConnection(true);
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
  }

}
