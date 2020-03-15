package com.robertsanek.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;

import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;

public class DateTimeUtils {

  //ALL DATES IN RANGEMAP ARE ASSUMED TO BE UTC
  private static final RangeMap<LocalDate, ZoneId> dateRangeMap = TreeRangeMap.create();
  private static final LocalDate MOVED_TO_CA = LocalDate.of(2015, 8, 1);
  private static final LocalDate JAPAN_KOREA_2018_START = LocalDate.of(2018, 7, 25);
  private static final LocalDate UAE_2018_START = LocalDate.of(2018, 8, 16);
  private static final LocalDate CZECH_2018_START = LocalDate.of(2018, 8, 23);
  private static final LocalDate CZECH_2018_END = LocalDate.of(2018, 9, 5);
  private static final LocalDate EGYPT_2018_START = LocalDate.of(2018, 12, 6);
  private static final LocalDate INDIA_2018_START = LocalDate.of(2018, 12, 16);
  private static final LocalDate HUNTSVILLE_2018_XMAS_START = LocalDate.of(2018, 12, 24);
  private static final LocalDate HUNTSVILLE_2018_XMAS_END = LocalDate.of(2019, 1, 2);
  private static final LocalDate CZECH_ITALY_2019_START = LocalDate.of(2019, 7, 8);
  private static final LocalDate CZECH_ITALY_2019_END = LocalDate.of(2019, 7, 30);
  private static final LocalDate HUNTSVILLE_2019_OCTOBER_START = LocalDate.of(2019, 10, 6);
  private static final LocalDate HUNTSVILLE_2019_OCTOBER_END = LocalDate.of(2019, 10, 15);
  private static final LocalDate HUNTSVILLE_2019_XMAS_START = LocalDate.of(2019, 12, 2);
  private static final LocalDate HUNTSVILLE_2019_XMAS_END = LocalDate.of(2020, 1, 1);
  private static final LocalDate INDIA_UAE_2020_FEBRUARY_START = LocalDate.of(2020, 2, 28);
  private static final LocalDate INDIA_UAE_2020_MARCH_END = LocalDate.of(2020, 3, 7);

  static {
    dateRangeMap.put(Range.lessThan(MOVED_TO_CA), ZoneId.of("America/Chicago")); //Huntsville, Auburn

    dateRangeMap.put(Range.closedOpen(MOVED_TO_CA, JAPAN_KOREA_2018_START), ZoneId.of("America/Los_Angeles"));

    dateRangeMap.put(Range.closedOpen(JAPAN_KOREA_2018_START, UAE_2018_START), ZoneId.of("Asia/Tokyo"));
    dateRangeMap.put(Range.closedOpen(UAE_2018_START, CZECH_2018_START), ZoneId.of("Asia/Yerevan"));
    dateRangeMap.put(Range.closedOpen(CZECH_2018_START, CZECH_2018_END), ZoneId.of("Europe/Paris"));

    dateRangeMap.put(Range.closedOpen(CZECH_2018_END, EGYPT_2018_START), ZoneId.of("America/Los_Angeles"));

    //can't find corresponding zoneid for Egypt
    dateRangeMap.put(Range.closedOpen(EGYPT_2018_START, INDIA_2018_START), ZoneOffset.ofHours(2));
    dateRangeMap.put(Range.closedOpen(INDIA_2018_START, HUNTSVILLE_2018_XMAS_START), ZoneId.of("Asia/Kolkata"));
    dateRangeMap
        .put(Range.closedOpen(HUNTSVILLE_2018_XMAS_START, HUNTSVILLE_2018_XMAS_END), ZoneId.of("America/Chicago"));

    dateRangeMap
        .put(Range.closedOpen(HUNTSVILLE_2018_XMAS_END, CZECH_ITALY_2019_START), ZoneId.of("America/Los_Angeles"));

    dateRangeMap
        .put(Range.closedOpen(CZECH_ITALY_2019_START, CZECH_ITALY_2019_END), ZoneId.of("Europe/Paris"));

    dateRangeMap.put(
        Range.closedOpen(CZECH_ITALY_2019_END, HUNTSVILLE_2019_OCTOBER_START), ZoneId.of("America/Los_Angeles"));
    dateRangeMap.put(
        Range.closedOpen(HUNTSVILLE_2019_OCTOBER_START, HUNTSVILLE_2019_OCTOBER_END), ZoneId.of("America/Chicago"));

    dateRangeMap.put(
        Range.closedOpen(HUNTSVILLE_2019_OCTOBER_END, HUNTSVILLE_2019_XMAS_START), ZoneId.of("America/Los_Angeles"));
    dateRangeMap.put(
        Range.closedOpen(HUNTSVILLE_2019_XMAS_START, HUNTSVILLE_2019_XMAS_END), ZoneId.of("America/Chicago"));

    dateRangeMap.put(
        Range.closedOpen(HUNTSVILLE_2019_XMAS_END, INDIA_UAE_2020_FEBRUARY_START), ZoneId.of("America/Los_Angeles"));
    dateRangeMap.put(
        Range.closedOpen(INDIA_UAE_2020_FEBRUARY_START, INDIA_UAE_2020_MARCH_END), ZoneId.of("Asia/Kolkata"));

    dateRangeMap.put(Range.atLeast(INDIA_UAE_2020_MARCH_END), ZoneId.of("America/Los_Angeles"));
  }

  private static ZoneId getZoneId(LocalDate localDate) {
    return Optional.ofNullable(dateRangeMap.get(localDate))
        .orElseThrow(() -> new RuntimeException(String.format("No mapping for date %s.", localDate)));
  }

  public static ZonedDateTime toZonedDateTime(LocalDateTime localDateTime) {
    return Optional.ofNullable(localDateTime)
        .map(ldt -> ZonedDateTime.of(localDateTime, getZoneId(localDateTime.toLocalDate())))
        .orElse(null);
  }

  public static ZonedDateTime toZonedDateTime(LocalDate localDate) {
    return Optional.ofNullable(localDate)
        .map(ld -> toZonedDateTime(LocalDateTime.of(ld, LocalTime.of(0, 0))))
        .orElse(null);
  }

  public static ZonedDateTime toZonedDateTime(OffsetDateTime offsetDateTime) {
    return Optional.ofNullable(offsetDateTime)
        .map(OffsetDateTime::toZonedDateTime)
        .orElse(null);
  }

  public static ZonedDateTime toZonedDateTime(Instant maybeInstant) {
    return Optional.ofNullable(maybeInstant)
        .map(instant -> {
          ZonedDateTime zdtUtc = instant.atZone(ZoneId.of("UTC"));
          return zdtUtc.withZoneSameInstant(getZoneId(zdtUtc.toLocalDate()));
        })
        .orElse(null);
  }

}
