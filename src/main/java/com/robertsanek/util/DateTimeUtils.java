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
  private static final LocalDate EGYPT_2018_START = LocalDate.of(2018, 12, 6);
  private static final LocalDate INDIA_2018_START = LocalDate.of(2018, 12, 16);
  private static final LocalDate HUNTSVILLE_2018_XMAS_START = LocalDate.of(2018, 12, 24);
  private static final LocalDate HUNTSVILLE_2018_XMAS_END = LocalDate.of(2019, 1, 2);

  static {
    dateRangeMap.put(Range.lessThan(MOVED_TO_CA), ZoneId.of("America/Chicago")); //Huntsville, Auburn
    dateRangeMap.put(Range.closedOpen(MOVED_TO_CA, EGYPT_2018_START), ZoneId.of("America/Los_Angeles"));
    //can't find corresponding zoneid for Egypt
    dateRangeMap.put(Range.closedOpen(EGYPT_2018_START, INDIA_2018_START), ZoneOffset.ofHours(2));
    dateRangeMap.put(Range.closedOpen(INDIA_2018_START, HUNTSVILLE_2018_XMAS_START), ZoneId.of("Asia/Kolkata"));
    dateRangeMap
        .put(Range.closedOpen(HUNTSVILLE_2018_XMAS_START, HUNTSVILLE_2018_XMAS_END), ZoneId.of("America/Chicago"));
    dateRangeMap.put(Range.atLeast(HUNTSVILLE_2018_XMAS_END), ZoneId.of("America/Los_Angeles"));
  }

  public static ZonedDateTime toZonedDateTime(LocalDateTime localDateTime) {
    if (localDateTime == null) {
      return null;
    }
    Optional<ZoneId> zoneId = Optional.ofNullable(dateRangeMap.get(localDateTime.toLocalDate()));
    return ZonedDateTime.of(localDateTime, zoneId.orElseThrow());
  }

  public static ZonedDateTime toZonedDateTime(LocalDate localDate) {
    if (localDate == null) {
      return null;
    }
    return toZonedDateTime(LocalDateTime.of(localDate, LocalTime.of(0, 0)));
  }

  public static ZonedDateTime toZonedDateTime(OffsetDateTime offsetDateTime) {
    if (offsetDateTime == null) {
      return null;
    }
    return offsetDateTime.toZonedDateTime();
  }

  public static ZonedDateTime toZonedDateTime(Instant instant) {
    if (instant == null) {
      return null;
    }
    ZonedDateTime zdtUtc = instant.atZone(ZoneId.of("UTC"));
    Optional<ZoneId> zoneId = Optional.ofNullable(dateRangeMap.get(zdtUtc.toLocalDate()));
    return zdtUtc.withZoneSameInstant(zoneId.orElseThrow());
  }

}
