package com.robertsanek.util;

import static org.junit.Assert.assertEquals;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

public class DateTimeUtilsTest {

  private final LocalDateTime ldtNow = LocalDateTime.now();
  private final LocalDate ldNow = LocalDate.now();

  @Test
  public void shouldWorkForAllDates() {
    LocalDate currentDate = LocalDate.of(2015, 8, 1);
    while (currentDate.isBefore(ldNow)) {
      DateTimeUtils.toZonedDateTime(currentDate);
      currentDate = currentDate.plusDays(1);
    }
  }

  @Test
  public void toZonedDateTime_now() {
    assertEquals(ZonedDateTime.of(ldtNow, ZoneId.of("America/Los_Angeles")), DateTimeUtils.toZonedDateTime(ldtNow));
    assertEquals(ZonedDateTime.of(LocalDateTime.of(ldNow, LocalTime.of(0, 0)), ZoneId.of("America/Los_Angeles")),
        DateTimeUtils.toZonedDateTime(ldNow));
  }

  @Test
  public void toZonedDateTime_past() {
    assertEquals(ZonedDateTime.of(2010, 1, 1, 0, 0, 0, 0, ZoneId.of("America/Chicago")),
        DateTimeUtils.toZonedDateTime(LocalDateTime.of(2010, 1, 1, 0, 0)));
  }

  @Test
  public void toZonedDateTime_future() {
    assertEquals(ZonedDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneId.of("America/Los_Angeles")),
        DateTimeUtils.toZonedDateTime(LocalDateTime.of(2020, 1, 1, 0, 0)));
  }

  @Test
  public void toZonedDateTime_india() {
    assertEquals(ZonedDateTime.of(2018, 12, 20, 0, 0, 0, 0, ZoneId.of("Asia/Kolkata")),
        DateTimeUtils.toZonedDateTime(LocalDateTime.of(2018, 12, 20, 0, 0)));
  }

  @Test
  public void toZonedDateTime_anki() {
    assertEquals(ZonedDateTime.of(2019, 1, 30, 23, 0, 0, 0, ZoneId.of("America/Los_Angeles")),
        DateTimeUtils.toZonedDateTime(Instant.ofEpochMilli(1548918000000L)));
  }

}