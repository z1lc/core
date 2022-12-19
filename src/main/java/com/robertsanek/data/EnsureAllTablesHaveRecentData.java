package com.robertsanek.data;

import static com.robertsanek.util.SecretType.RENDER_SQL_Z_BI_POSTGRES_PASSWORD;
import static com.robertsanek.util.SecretType.RENDER_SQL_Z_BI_POSTGRES_USERNAME;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Table;

import org.apache.commons.lang3.tuple.Pair;
import org.reflections.Reflections;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;
import com.robertsanek.util.NotificationSender;
import com.robertsanek.util.SecretProvider;
import com.robertsanek.util.Unchecked;

public class EnsureAllTablesHaveRecentData {

  private static final Log log = Logs.getLog(EnsureAllTablesHaveRecentData.class);
  private static final Period DEFAULT_MAX_STALENESS = Period.ofDays(14);
  private static final ImmutableMap<Pair<String, String>, Period> customMaxStaleness =
      ImmutableMap.<Pair<String, String>, Period>builder()
          .put(Pair.of("credit_scores", "date"), Period.ofDays(60))
          .put(Pair.of("blood_pressure_readings", "date"), Period.ofDays(60))
          .put(Pair.of("trello_cards", "last_activity"), Period.ofDays(30))
          .put(Pair.of("goodreads_books", "added_on"), Period.ofDays(30))
          .put(Pair.of("goodreads_books", "read_at"), Period.ofDays(30))
          .build();
  private static final ImmutableSet<Pair<String, String>> infiniteStalenessColumn =
      ImmutableSet.<Pair<String, String>>builder()
          .add(Pair.of("anki_models", "created_at"))
          .add(Pair.of("anki_cards", "last_modified_at"))
          .add(Pair.of("workflowy_entries", "date_exported"))
          .build();
  private static final ImmutableSet<String> infiniteStalenessTable =
      ImmutableSet.<String>builder()
          .add("nokia_readings") // currently very stale because of Human API broken-ness
          .add("toodledo_habits")  //migrated to Habitica
          .add("toodledo_habit_repetitions")
          .add("habitica_histories") // no longer using
          .add("toodledo_tasks")  //no longer using
          .build();

  @Inject SecretProvider secretProvider;
  @Inject NotificationSender notificationSender;

  public void ensure() {
    Set<String> violations = Sets.newHashSet();
    String jdbcUrl = "jdbc:postgresql://dpg-cefuij1gp3jk7mi5qev0-a.oregon-postgres.render.com:5432/z_bi";
    String username = secretProvider.getSecret(RENDER_SQL_Z_BI_POSTGRES_USERNAME);
    String password = secretProvider.getSecret(RENDER_SQL_Z_BI_POSTGRES_PASSWORD);

    Unchecked.run(() -> Class.forName("org.postgresql.Driver"));
    try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
      try (Statement statement = connection.createStatement()) {
        new Reflections("com.robertsanek").getTypesAnnotatedWith(Table.class)
            .forEach(clazz -> {
              String tableName = clazz.getAnnotation(Table.class).name();
              if (infiniteStalenessTable.contains(tableName)) {
                return;
              }
              Set<String> dateColumns = Arrays.stream(clazz.getDeclaredFields())
                  .filter(field -> field.getType().equals(ZonedDateTime.class))
                  .map(zdtField -> Optional.ofNullable(zdtField.getAnnotation(Column.class))
                      .map(Column::name)
                      .orElse(zdtField.getName().toLowerCase()))
                  .collect(Collectors.toSet());

              dateColumns.stream()
                  .filter(dateColumn -> !infiniteStalenessColumn.contains(Pair.of(tableName, dateColumn)))
                  .forEach(dateColumn -> {
                    try {
                      ResultSet resultSet =
                          statement.executeQuery(String.format("SELECT MAX(%s) FROM %s;", dateColumn, tableName));
                      resultSet.next();
                      Optional<LocalDate> maxDate = Optional.ofNullable(resultSet.getString(1))
                          .map(dateString -> LocalDate.parse(dateString.substring(0, 10)));
                      if (maxDate
                          .map(date -> date.isBefore(LocalDate.now().minus(
                              customMaxStaleness.getOrDefault(Pair.of(tableName, dateColumn), DEFAULT_MAX_STALENESS))))
                          .orElse(true)) {
                        violations.add(String.format(
                            "Column '%s' in table '%s' potentially has stale data: latest date is %s.",
                            dateColumn, tableName, maxDate.orElse(null)));
                      }
                    } catch (SQLException e) {
                      log.error(e);
                    }
                  });
            });
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    if (violations.size() > 0) {
      notificationSender.sendEmailDefault("Potentially stale data!", String.join("<br>", violations));
    }
  }
}
