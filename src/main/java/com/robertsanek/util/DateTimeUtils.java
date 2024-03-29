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
  private static final LocalDate HUNTSVILLE_2020_COVID_START = LocalDate.of(2020, 3, 17);
  private static final LocalDate HUNTSVILLE_2020_COVID_END = LocalDate.of(2020, 10, 18);
  private static final LocalDate HUNTSVILLE_2020_XMAS_START = LocalDate.of(2020, 12, 21);
  private static final LocalDate HUNTSVILLE_2020_XMAS_END = LocalDate.of(2021, 1, 4);
  private static final LocalDate MEXICO_2021_START = LocalDate.of(2021, 2, 5);
  private static final LocalDate MEXICO_2021_END = LocalDate.of(2021, 2, 28);
  private static final LocalDate NYC_2021_START = LocalDate.of(2021, 4, 16);
  private static final LocalDate NYC_2021_END = LocalDate.of(2021, 5, 9);
  private static final LocalDate MEXICO_2021_P2_START = LocalDate.of(2021, 7, 10);
  private static final LocalDate MEXICO_2021_P2_END = LocalDate.of(2021, 7, 18);
  private static final LocalDate HUNTSVILLE_2021_START = LocalDate.of(2021, 9, 22);
  private static final LocalDate HUNTSVILLE_2021_END_NYC_P2_START = LocalDate.of(2021, 9, 27);
  private static final LocalDate NYC_2021_P2_END = LocalDate.of(2021, 10, 6);
  private static final LocalDate HUNTSVILLE_2021_P2_START = LocalDate.of(2021, 11, 11);
  private static final LocalDate HUNTSVILLE_2021_P2_END = LocalDate.of(2021, 11, 15);
  private static final LocalDate HUNTSVILLE_2022_START = LocalDate.of(2022, 1, 1);
  private static final LocalDate HUNTSVILLE_2022_END = LocalDate.of(2022, 1, 11);
  private static final LocalDate MEXICO_2022_START = LocalDate.of(2022, 2, 4);
  private static final LocalDate MEXICO_2022_END = LocalDate.of(2022, 2, 21);
  private static final LocalDate EUROPE_2022_START = LocalDate.of(2022, 7, 21);
  private static final LocalDate EUROPE_2022_END = LocalDate.of(2022, 8, 15);
  private static final LocalDate NYC_2022_START = LocalDate.of(2022, 9, 1);
  private static final LocalDate NYC_2022_END = LocalDate.of(2022, 10, 2);
  private static final LocalDate PERU_2022_START = LocalDate.of(2022, 11, 16);
  private static final LocalDate PERU_2022_END = LocalDate.of(2022, 12, 7);
  private static final LocalDate HUNTSVILLE_2022_P2_START = LocalDate.of(2022, 12, 20);
  private static final LocalDate HUNTSVILLE_2022_P2_END_VIETNAM_2023_START = LocalDate.of(2022, 12, 29);
  private static final LocalDate VIETNAM_2023_END_INDONESIA_2023_START = LocalDate.of(2023, 1, 8);
  private static final LocalDate INDONESIA_2023_END = LocalDate.of(2023, 1, 19);
  private static final LocalDate NYC_2023_START = LocalDate.of(2023, 3, 16);

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

    dateRangeMap.put(
        Range.closedOpen(INDIA_UAE_2020_MARCH_END, HUNTSVILLE_2020_COVID_START), ZoneId.of("America/Los_Angeles"));
    dateRangeMap.put(
        Range.closedOpen(HUNTSVILLE_2020_COVID_START, HUNTSVILLE_2020_COVID_END), ZoneId.of("America/Chicago"));

    dateRangeMap.put(
        Range.closedOpen(HUNTSVILLE_2020_COVID_END, HUNTSVILLE_2020_XMAS_START), ZoneId.of("America/Los_Angeles"));
    dateRangeMap.put(
        Range.closedOpen(HUNTSVILLE_2020_XMAS_START, HUNTSVILLE_2020_XMAS_END), ZoneId.of("America/Chicago"));

    dateRangeMap.put(
        Range.closedOpen(HUNTSVILLE_2020_XMAS_END, MEXICO_2021_START), ZoneId.of("America/Los_Angeles"));
    dateRangeMap.put(
        Range.closedOpen(MEXICO_2021_START, MEXICO_2021_END), ZoneId.of("America/Chicago"));

    dateRangeMap.put(
        Range.closedOpen(MEXICO_2021_END, NYC_2021_START), ZoneId.of("America/Los_Angeles"));
    dateRangeMap.put(
        Range.closedOpen(NYC_2021_START, NYC_2021_END), ZoneId.of("America/New_York"));

    dateRangeMap.put(
        Range.closedOpen(NYC_2021_END, MEXICO_2021_P2_START), ZoneId.of("America/Los_Angeles"));
    dateRangeMap.put(
        Range.closedOpen(MEXICO_2021_P2_START, MEXICO_2021_P2_END), ZoneId.of("America/Cancun"));
    dateRangeMap.put(
        Range.closedOpen(MEXICO_2021_P2_END, HUNTSVILLE_2021_START), ZoneId.of("America/Los_Angeles"));
    dateRangeMap.put(
        Range.closedOpen(HUNTSVILLE_2021_START, HUNTSVILLE_2021_END_NYC_P2_START), ZoneId.of("America/Chicago"));
    dateRangeMap.put(
        Range.closedOpen(HUNTSVILLE_2021_END_NYC_P2_START, NYC_2021_P2_END), ZoneId.of("America/New_York"));
    dateRangeMap.put(
        Range.closedOpen(NYC_2021_P2_END, HUNTSVILLE_2021_P2_START), ZoneId.of("America/Los_Angeles"));
    dateRangeMap.put(
        Range.closedOpen(HUNTSVILLE_2021_P2_START, HUNTSVILLE_2021_P2_END), ZoneId.of("America/Chicago"));
    dateRangeMap.put(
        Range.closedOpen(HUNTSVILLE_2021_P2_END, HUNTSVILLE_2022_START), ZoneId.of("America/Los_Angeles"));
    dateRangeMap.put(
        Range.closedOpen(HUNTSVILLE_2022_START, HUNTSVILLE_2022_END), ZoneId.of("America/Chicago"));
    dateRangeMap.put(
        Range.closedOpen(HUNTSVILLE_2022_END, MEXICO_2022_START), ZoneId.of("America/Los_Angeles"));
    dateRangeMap.put(
        Range.closedOpen(MEXICO_2022_START, MEXICO_2022_END), ZoneId.of("America/Cancun"));
    dateRangeMap.put(
        Range.closedOpen(MEXICO_2022_END, EUROPE_2022_START), ZoneId.of("America/Los_Angeles"));
    dateRangeMap.put(
        Range.closedOpen(EUROPE_2022_START, EUROPE_2022_END), ZoneId.of("UTC+1"));
    dateRangeMap.put(
        Range.closedOpen(EUROPE_2022_END, NYC_2022_START), ZoneId.of("America/Los_Angeles"));
    dateRangeMap.put(
        Range.closedOpen(NYC_2022_START, NYC_2022_END), ZoneId.of("America/New_York"));
    dateRangeMap.put(
        Range.closedOpen(NYC_2022_END, PERU_2022_START), ZoneId.of("America/Los_Angeles"));
    dateRangeMap.put(
        Range.closedOpen(PERU_2022_START, PERU_2022_END), ZoneId.of("UTC-5"));
    dateRangeMap.put(
        Range.closedOpen(PERU_2022_END, HUNTSVILLE_2022_P2_START), ZoneId.of("America/Los_Angeles"));
    dateRangeMap.put(
        Range.closedOpen(HUNTSVILLE_2022_P2_START, HUNTSVILLE_2022_P2_END_VIETNAM_2023_START), ZoneId.of("America/Chicago"));
    dateRangeMap.put(
        Range.closedOpen(HUNTSVILLE_2022_P2_END_VIETNAM_2023_START, VIETNAM_2023_END_INDONESIA_2023_START), ZoneId.of("UTC+7"));
    dateRangeMap.put(
        Range.closedOpen(VIETNAM_2023_END_INDONESIA_2023_START, INDONESIA_2023_END), ZoneId.of("UTC+8"));
    dateRangeMap.put(
        Range.closedOpen(INDONESIA_2023_END, NYC_2023_START), ZoneId.of("America/Los_Angeles"));

    dateRangeMap.put(Range.atLeast(NYC_2023_START), ZoneId.of("America/New_York"));
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
