package com.robertsanek.process;

import static com.robertsanek.process.MasterEtl.REFLECTIONS_PREFIX;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Entity;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import com.google.common.collect.ImmutableSet;
import com.robertsanek.data.derived.anki.BacklogForDay;
import com.robertsanek.data.etl.local.sqllite.anki.ReviewEtl;
import com.robertsanek.data.etl.local.workflowy.WorkflowyEtl;
import com.robertsanek.data.etl.remote.fitbit.json.Sleep;
import com.robertsanek.data.etl.remote.google.analytics.PageView;
import com.robertsanek.data.etl.remote.google.sheets.health.Health;
import com.robertsanek.data.etl.remote.humanapi.entities.ActivitySummary;
import com.robertsanek.data.etl.remote.humanapi.entities.SleepSummary;
import com.robertsanek.data.etl.remote.wikipedia.WikiPerson;
import com.robertsanek.util.inject.InjectUtils;

public class MasterEtlTest {

  /* https://www.postgresql.org/docs/9.1/datatype-datetime.html
   * 2 types in PostgreSQL:
   * timestamp with time zone
   * timestamp without time zone
   * docs say "we do not recommend using the type time with time zone", so we must use type timestamp without time zone
   * The ZonedDateTime Java type forces you to specify time zone when creating objects, but will be mapped to the
   * timestamp without time zone type, which is what we want. Other types do not force you to specify time zone
   * (Instant, LocalDate, LocalTime, LocalDateTime), or use an offset instead of time zone (OffsetDateTime), which is
   * not quite the same thing and can cause DST confusion.
   */
  private static final ImmutableSet<Class<?>> DISALLOWED_TIMES = ImmutableSet.of(
      Instant.class,
      LocalDate.class,
      LocalTime.class,
      LocalDateTime.class,
      OffsetDateTime.class
  );

  private static final ImmutableSet<Pair<Class<?>, Class<?>>> EXCEPTIONS = ImmutableSet.of(
      Pair.of(SleepSummary.class, LocalDate.class),
      Pair.of(ActivitySummary.class, LocalDate.class),
      Pair.of(WikiPerson.class, LocalDate.class),
      Pair.of(Sleep.class, LocalDateTime.class),
      Pair.of(Sleep.class, LocalDate.class),
      Pair.of(PageView.class, LocalDate.class),
      Pair.of(Health.class, LocalDate.class),
      Pair.of(BacklogForDay.class, LocalDate.class)
  );

  @Test
  @Disabled("integration")
  public void integration_test() {
    InjectUtils.inject(MasterEtl.class).exec(null);
  }

  @Test
  public void getConcreteEtls_filtersOutDoNotRun() {
    assertFalse(new MasterEtl().getConcreteEtls(false).contains(WorkflowyEtl.class));
    assertFalse(new MasterEtl().getConcreteEtls(true).contains(WorkflowyEtl.class));
  }

  @Test
  public void getConcreteEtls_filtersOutSlowEtl() {
    assertTrue(new MasterEtl().getConcreteEtls(false).contains(ReviewEtl.class));
    assertFalse(new MasterEtl().getConcreteEtls(true).contains(ReviewEtl.class));
  }

  @Test
  public void allDateTimesHaveTimeZone() {
    Reflections reflections = new Reflections(REFLECTIONS_PREFIX);
    Set<Class<?>> subTypesOf = reflections.getTypesAnnotatedWith(Entity.class);
    List<String> violations = subTypesOf.stream()
        .flatMap(clazz -> Arrays.stream(clazz.getDeclaredFields()).map(field -> Pair.of(clazz, field.getType())))
        .filter(pair -> DISALLOWED_TIMES.contains(pair.getRight()))
        .filter(pair -> !EXCEPTIONS.contains(pair))
        .map(pair -> String.format("%s: %s", pair.getLeft(), pair.getRight()))
        .distinct()
        .collect(Collectors.toList());
    if (violations.size() > 0) {
      throw new RuntimeException(
          String.format("%s classes have fields that refer to date and/or time but have no time zone:\n%s",
              violations.size(), String.join("\n", violations)));
    }
  }

}